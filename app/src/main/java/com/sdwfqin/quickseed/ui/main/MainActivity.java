package com.sdwfqin.quickseed.ui.main;

import android.Manifest;
import android.view.View;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.blankj.utilcode.util.ConvertUtils;
import com.qmuiteam.qmui.widget.tab.QMUITab;
import com.qmuiteam.qmui.widget.tab.QMUITabBuilder;
import com.qmuiteam.qmui.widget.tab.QMUITabSegment;
import com.sdwfqin.quickseed.R;
import com.sdwfqin.quickseed.base.SampleBaseActivity;
import com.sdwfqin.widget.NoScrollViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 主Acticity
 * <p>
 *
 * @author 张钦
 * @date 2020-01-15
 */
public class MainActivity extends SampleBaseActivity {

    @BindView(R.id.pager)
    NoScrollViewPager mViewPager;
    @BindView(R.id.tabs)
    QMUITabSegment mTabs;

    private List<Fragment> mPages;
    private TabPagerAdapter mTabPagerAdapter;

    @Override
    protected int getLayout() {
        return R.layout.activity_main;
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

    private void initPagers() {
        mPages = new ArrayList<>(2);
        mPages.add(new MainFragment());
        mPages.add(new SettingFragment());

        mTabPagerAdapter = new TabPagerAdapter(getSupportFragmentManager(), mPages);

        mViewPager.setOffscreenPageLimit(2);
        mViewPager.setAdapter(mTabPagerAdapter);
    }

    private void initTabs() {
        QMUITabBuilder builder = mTabs.tabBuilder();
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

        mTabs.addTab(component)
                .addTab(util);

        mTabs.setupWithViewPager(mViewPager, false);
    }

    /**
     * 获取权限
     */
    private void getPermissions() {

        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
        initCheckPermissions(perms, true, false, new OnPermissionCallback() {
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

    private class TabPagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> pagers;

        public TabPagerAdapter(FragmentManager fm, List<Fragment> pagers) {
            super(fm);
            this.pagers = pagers;
        }

        @Override
        public Fragment getItem(int position) {
            return pagers.get(position);
        }

        @Override
        public int getCount() {
            return pagers.size();
        }
    }

}
