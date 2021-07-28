package io.github.sdwfqin.quicklib.imagepreview

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.blankj.utilcode.util.*
import com.google.android.material.snackbar.Snackbar
import io.github.sdwfqin.imageloader.ImageLoaderManager
import io.github.sdwfqin.imageloader.progress.OnProgressListener
import io.github.sdwfqin.quicklib.R
import io.github.sdwfqin.quicklib.base.BaseFragment
import io.github.sdwfqin.quicklib.base.QuickConstants
import io.github.sdwfqin.quicklib.databinding.QuickFragmentImagePreviewBinding
import java.io.File

/**
 * 描述：显示图片
 *
 * @author zhangqin
 * @date 2017/8/7
 */
class ImagePreviewFragment : BaseFragment<QuickFragmentImagePreviewBinding>() {

    private lateinit var url: String

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): QuickFragmentImagePreviewBinding {
        return QuickFragmentImagePreviewBinding.inflate(inflater)
    }

    override fun initEventAndData() {
        arguments?.let {
            url = it.getString(ARG_URL)!!
        }
        ImageLoaderManager.Builder()
            .setImagePath(url)
            .setPlaceholder(R.mipmap.image_loading)
            .setErrorImage(R.mipmap.image_load_err)
            .build(mBinding.image)
            .loadImage()
            .setOnProgressListener(object : OnProgressListener {
                override fun onLoading(progress: Int) {
                    mBinding.progressBar.visibility = View.VISIBLE
                    mBinding.progressBar.progress = progress
                }

                override fun onLoadSuccess() {
                    mBinding.progressBar.visibility = View.GONE
                }
            })

        // setOnLongClickListener中return的值决定是否在长按后再加一个短按动作
        // true为不加短按,false为加入短按
        mBinding.image.setOnLongClickListener {
            initSaveImageDialog()
            false
        }
        mBinding.image.setOnClickListener { mBaseActivity.getActivity().finish() }
    }

    override fun initClickListener() {}

    private fun initSaveImageDialog() {
        val bottomSheetDialogFragment = BottomDialogImagePreviewFragment.Builder()
            .setOnClickListener(object : BottomDialogImagePreviewFragment.OnDialogClickListener {
                override fun save() {
                    saveImage()
                }

                override fun exit() {}
            })
            .builder()
        bottomSheetDialogFragment.show(childFragmentManager, "quick_preview_image")
    }

    /**
     * 保存图片
     */
    fun saveImage() {
        if (SDCardUtils.isSDCardEnableByEnvironment()) {
            try {
                val filePath =
                    QuickConstants.SAVE_REAL_PATH + url.substring(url.lastIndexOf("/"))
                if (FileUtils.createOrExistsFile(filePath)) {
                    val bitmapDrawable = mBinding.image.drawable as BitmapDrawable
                    val bitmap2Bytes = ImageUtils.bitmap2Bytes(
                        bitmapDrawable.bitmap,
                        Bitmap.CompressFormat.JPEG,
                        100
                    )
                    if (FileIOUtils.writeFileFromBytesByStream(filePath, bitmap2Bytes)) {
                        Snackbar.make(
                            mBinding.root,
                            R.string.quick_img_save_success,
                            Snackbar.LENGTH_SHORT
                        ).show()
                        val contentUri = Uri.fromFile(File(filePath))
                        val mediaScanIntent =
                            Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, contentUri)
                        mContext.sendBroadcast(mediaScanIntent)
                    } else {
                        throw Exception(getString(R.string.quick_img_save_error))
                    }
                } else {
                    throw Exception(getString(R.string.quick_img_save_error))
                }
            } catch (e: Exception) {
                LogUtils.e(e)
                mBaseActivity.showMsg(e.message!!)
            }
        } else {
            mBaseActivity.showMsg(getString(R.string.quick_sd_not_found))
        }
    }

    companion object {

        private const val ARG_URL = "url"

        fun newInstance(url: String): Fragment {
            val bundle = Bundle()
            bundle.putString(ARG_URL, url)
            val fragment = ImagePreviewFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}