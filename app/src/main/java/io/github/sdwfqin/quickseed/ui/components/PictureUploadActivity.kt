package io.github.sdwfqin.quickseed.ui.components

import android.content.Intent
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.zhihu.matisse.Matisse
import io.github.sdwfqin.quickseed.constants.ArouterConstants
import io.github.sdwfqin.quickseed.data.bean.PictureModel
import io.github.sdwfqin.quickseed.databinding.ActivityPictureUploadBinding
import io.github.sdwfqin.samplecommonlibrary.base.SampleBaseActivity
import io.github.sdwfqin.samplecommonlibrary.utils.picture.PictureSelectUtils
import io.github.sdwfqin.widget.pictureupload.PictureUploadCallback
import java.util.*

/**
 * 描述：九宫格上传图片
 *
 * @author zhangqin
 * @date 2018/5/31
 */
@Route(path = ArouterConstants.COMPONENTS_PICTUREUPLOAD)
class PictureUploadActivity : SampleBaseActivity<ActivityPictureUploadBinding>() {

    override fun getViewBinding(): ActivityPictureUploadBinding {
        return ActivityPictureUploadBinding.inflate(
            layoutInflater
        )
    }

    override fun initEventAndData() {
        mTopBar.setTitle("九宫格上传图片")
        mTopBar.addLeftBackImageButton()
            .setOnClickListener { v: View? -> finish() }
        mBinding.pic.setMaxColumn(3)
        mBinding.pic.setMaxSize(12)
        mBinding.pic.setPicUploadCallback(object : PictureUploadCallback<PictureModel> {
            override fun click(
                position: Int,
                item: PictureModel,
                list: List<PictureModel>
            ) {

            }

            override fun remove(position: Int, list: List<PictureModel>): Boolean {
                return true
            }

            override fun onAddPic(maxPic: Int, list: List<PictureModel>) {
                PictureSelectUtils.SelectSystemPhoto(mContext, RESULT_PHOTO_SELECT, maxPic)
            }
        })
    }

    override fun initClickListener() {}

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                RESULT_PHOTO_SELECT -> {
                    val models: MutableList<PictureModel> = ArrayList()
                    val selectList = Matisse.obtainResult(data)
                    var i = 0
                    while (i < selectList.size) {
                        models.add(PictureModel(selectList[i]))
                        i++
                    }
                    mBinding!!.pic.addData(models)

                    // 刷新相册图片
                    if (selectList.size == 1) {
                        val contentUri = selectList[0]
                        val mediaScanIntent =
                            Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, contentUri)
                        sendBroadcast(mediaScanIntent)
                    }
                }
                else -> {
                }
            }
        }
    }

    companion object {
        const val RESULT_PHOTO_SELECT = 101
    }
}