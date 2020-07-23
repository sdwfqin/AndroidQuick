package com.sdwfqin.quickseed.ui.main;

import android.Manifest;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.ConvertUtils;
import com.qmuiteam.qmui.widget.tab.QMUITab;
import com.qmuiteam.qmui.widget.tab.QMUITabBuilder;
import com.sdwfqin.quickseed.R;
import com.sdwfqin.quickseed.base.ArouterConstants;
import com.sdwfqin.quickseed.base.SampleBaseActivity;
import com.sdwfqin.quickseed.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * 主Acticity
 * <p>
 *
 * @author 张钦
 * @date 2020-01-15
 */
public class MainActivity extends SampleBaseActivity<ActivityMainBinding> {

    private List<Fragment> mPages;
    private TabPagerAdapter mTabPagerAdapter;

    @Override
    protected ActivityMainBinding getViewBinding() {
        return ActivityMainBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void initEventAndData() {
        /**
         * 隐藏默认Bar
         */
        mTopBar.setVisibility(View.GONE);

        initPagers();
        initTabs();

        getPermissions();
    }

    @Override
    protected void initClickListener() {

    }

    private void initPagers() {
        mPages = new ArrayList<>(2);
        mPages.add((Fragment) ARouter.getInstance().build(ArouterConstants.MAIN_HOME).navigation());
        mPages.add((Fragment) ARouter.getInstance().build(ArouterConstants.MAIN_MINE).navigation());

        mTabPagerAdapter = new TabPagerAdapter(getSupportFragmentManager(), getLifecycle(), mPages);

        mBinding.pager.setOffscreenPageLimit(2);
        mBinding.pager.setAdapter(mTabPagerAdapter);

        // 禁止ViewPager2的滑动
        mBinding.pager.setUserInputEnabled(false);
    }

    private void initTabs() {
        QMUITabBuilder builder = mBinding.tabs.tabBuilder();
        builder.setSelectedIconScale(1.2f)
                .setTextSize(ConvertUtils.sp2px(13), ConvertUtils.sp2px(15))
                .setDynamicChangeIconColor(false);
        QMUITab component = builder
                .setNormalDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_home_apps_normal))
                .setSelectedDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_home_apps_selected))
                .setText("组件")
                .build(mContext);
        QMUITab util = builder
                .setNormalDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_home_settings_normal))
                .setSelectedDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_home_settings_selected))
                .setText("配置")
                .build(mContext);

        mBinding.tabs.addTab(component)
                .addTab(util);

        mBinding.tabs.setupWithViewPager(mBinding.pager);
    }

    /**
     * 获取权限
     */
    private void getPermissions() {

        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
        initCheckPermissions(perms, new OnPermissionCallback() {
            @Override
            public void onSuccess() {
                Toast.makeText(mContext, "取得权限", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError() {
                Toast.makeText(mContext, "没有取得权限", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private class TabPagerAdapter extends FragmentStateAdapter {

        private List<Fragment> pagers;

        public TabPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, List<Fragment> pagers) {
            super(fragmentManager, lifecycle);
            this.pagers = pagers;
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return pagers.get(position);
        }

        @Override
        public int getItemCount() {
            return pagers.size();
        }
    }

}
