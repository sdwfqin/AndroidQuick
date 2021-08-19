package io.github.sdwfqin.quicklib.imagepreview

import android.annotation.SuppressLint
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.qmuiteam.qmui.util.QMUIStatusBarHelper
import io.github.sdwfqin.quicklib.R
import io.github.sdwfqin.quicklib.base.BaseActivity
import io.github.sdwfqin.quicklib.base.QuickArouterConstants
import io.github.sdwfqin.quicklib.databinding.QuickActivityImagePreviewBinding
import java.util.*

/**
 * 图片查看与保存Activity
 *
 * 需传入string类型的集合
 *
 * @author zhangqin
 * @date 2017/8/7
 */
@Route(path = QuickArouterConstants.QUICK_IMAGEPREVIEW)
class ImagePreviewActivity : BaseActivity<QuickActivityImagePreviewBinding>() {

    private lateinit var mImageList: List<String>
    private var position = 0

    override fun getViewBinding(): QuickActivityImagePreviewBinding {
        return QuickActivityImagePreviewBinding.inflate(layoutInflater)
    }

    override fun initEventAndData() {
//        QMUIStatusBarHelper.setStatusBarDarkMode(mContext)
        mNavBar.visibility = View.GONE
        intent.extras?.let {
            mImageList = intent.getStringArrayListExtra(ARG_DATA)!!
            position = intent.getIntExtra(ARG_POS, 0)
        } ?: let {
            showMsg(getString(R.string.quick_args_get_error))
            finish()
        }
        val showImagePagerAdapter = ShowImagePagerAdapter(supportFragmentManager, lifecycle)
        mBinding.viewpager.adapter = showImagePagerAdapter
        mBinding.viewpager.currentItem = position
        if (mImageList.size == 1) {
            mBinding.position.visibility = View.GONE
        } else {
            mBinding.position.visibility = View.VISIBLE
        }
        setPosText(position + 1, mImageList.size)
        mBinding.viewpager.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                setPosText(position + 1, mImageList.size)
            }
        })
    }

    override fun initClickListener() {}

    /**
     * 指示器
     *
     * @param position
     * @param size
     */
    fun setPosText(position: Int, size: Int) {
        mBinding.position.text = "$position/$size"
    }

    private inner class ShowImagePagerAdapter(
        fragmentManager: FragmentManager,
        lifecycle: Lifecycle
    ) : FragmentStateAdapter(fragmentManager, lifecycle) {
        override fun createFragment(position: Int): Fragment {
            return ImagePreviewFragment.newInstance(mImageList[position])
        }

        override fun getItemCount(): Int {
            return mImageList.size
        }
    }

    companion object {

        private const val ARG_DATA = "data"
        private const val ARG_POS = "position"

        /**
         * @param stringList
         * @param position   当前图片位置
         * @return
         */
        fun start(stringList: List<String>, position: Int = 0) {
            ARouter
                .getInstance()
                .build(QuickArouterConstants.QUICK_IMAGEPREVIEW)
                .withStringArrayList(ARG_DATA, stringList as ArrayList<String>)
                .withInt(ARG_POS, position)
                .navigation()
        }
    }
}