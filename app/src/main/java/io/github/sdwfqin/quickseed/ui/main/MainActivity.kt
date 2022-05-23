package io.github.sdwfqin.quickseed.ui.main

import android.Manifest
import android.os.Build
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.alibaba.android.arouter.launcher.ARouter
import com.blankj.utilcode.util.ConvertUtils
import io.github.sdwfqin.quickseed.R
import io.github.sdwfqin.quickseed.constants.ArouterConstants
import io.github.sdwfqin.quickseed.databinding.ActivityMainBinding
import io.github.sdwfqin.samplecommonlibrary.base.SampleBaseActivity

/**
 * 主页
 * <p>
 *
 * @author 张钦
 * @date 2020/8/3
 */
class MainActivity : SampleBaseActivity<ActivityMainBinding>() {

    private val mPages = ArrayList<Fragment>()
    private val mTabPagerAdapter by lazy {
        TabPagerAdapter(supportFragmentManager, lifecycle, mPages)
    }

    override fun getViewBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    override fun initEventAndData() {
        /**
         * 隐藏默认Bar
         */
        mNavBar.visibility = View.GONE

        initPagers()
        initTabs()

        getPermissions()
    }

    private fun initPagers() {
        mPages.add(
            (ARouter.getInstance().build(ArouterConstants.MAIN_HOME).navigation() as Fragment)
        )
        mPages.add(
            (ARouter.getInstance().build(ArouterConstants.MAIN_MINE).navigation() as Fragment)
        )

        mBinding.pager.offscreenPageLimit = 2
        mBinding.pager.adapter = mTabPagerAdapter

        // 禁止ViewPager2的滑动
        mBinding.pager.isUserInputEnabled = false
    }

    private fun initTabs() {
        val builder = mBinding.tabs.tabBuilder()
        builder.setSelectedIconScale(1.2f)
            .setTextSize(ConvertUtils.sp2px(13f), ConvertUtils.sp2px(15f))
            .setDynamicChangeIconColor(false)
        val component = builder
            .setNormalDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_home_apps_normal))
            .setSelectedDrawable(
                ContextCompat.getDrawable(
                    mContext,
                    R.drawable.ic_home_apps_selected
                )
            )
            .setText("组件")
            .build(mContext)
        val util = builder
            .setNormalDrawable(
                ContextCompat.getDrawable(
                    mContext,
                    R.drawable.ic_home_settings_normal
                )
            )
            .setSelectedDrawable(
                ContextCompat.getDrawable(
                    mContext,
                    R.drawable.ic_home_settings_selected
                )
            )
            .setText("配置")
            .build(mContext)
        mBinding.tabs.addTab(component)
            .addTab(util)
        mBinding.tabs.setupWithViewPager(mBinding.pager)
    }

    /**
     * 获取权限
     */
    private fun getPermissions() {
        val perms = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            arrayOf(
                Manifest.permission.CAMERA
            )
        } else {
            arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            )
        }
        initCheckPermissions(perms, object : OnPermissionCallback {
            override fun onSuccess() {
                Toast.makeText(mContext, "取得权限", Toast.LENGTH_SHORT).show()
            }

            override fun onError() {
                Toast.makeText(mContext, "没有取得权限", Toast.LENGTH_SHORT).show()
                finish()
            }
        })
    }

    private class TabPagerAdapter(
        fragmentManager: FragmentManager,
        lifecycle: Lifecycle,
        private val pagers: List<Fragment>
    ) : FragmentStateAdapter(fragmentManager, lifecycle) {
        override fun createFragment(position: Int): Fragment {
            return pagers[position]
        }

        override fun getItemCount(): Int {
            return pagers.size
        }
    }
}
