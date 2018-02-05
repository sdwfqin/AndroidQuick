package com.sdwfqin.quickseed.ui;

import android.Manifest;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;

import com.qmuiteam.qmui.util.QMUIResHelper;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITabSegment;
import com.sdwfqin.quicklib.base.BaseActivity;
import com.sdwfqin.quickseed.R;
import com.sdwfqin.quickseed.base.Constants;
import com.sdwfqin.quickseed.ui.find.FindFragment;
import com.sdwfqin.quickseed.ui.home.HomeFragment;
import com.sdwfqin.quickseed.ui.my.MyFragment;
import com.sdwfqin.quicklib.view.NoScrollViewPager;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * 描述：主Activity
 *
 * @author 张钦
 */
public class MainActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks {

    @BindView(R.id.pager)
    NoScrollViewPager mPager;
    @BindView(R.id.tabs)
    QMUITabSegment mTabs;

    private HashMap<Pager, Fragment> mPages;
    private MyFragPagerAdapter mPagerAdapter;

    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initEventAndData() {

        QMUIStatusBarHelper.translucent(mContext);
        QMUIStatusBarHelper.setStatusBarLightMode(mContext);

        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
        if (EasyPermissions.hasPermissions(this, perms)) {
        } else {
            EasyPermissions.requestPermissions(mContext, "App正常运行需要存储权限、媒体权限", Constants.RESULT_CODE_1, perms);
        }

        Constants.STATUS_HEIGHT = QMUIStatusBarHelper.getStatusbarHeight(mContext);

        initTabs();
        initPagers();

    }

    private void initTabs() {
        int normalColor = QMUIResHelper.getAttrColor(mContext, R.attr.qmui_config_color_gray_6);
        int selectColor = QMUIResHelper.getAttrColor(mContext, R.attr.qmui_config_color_blue);
        mTabs.setDefaultNormalColor(normalColor);
        mTabs.setDefaultSelectedColor(selectColor);
        QMUITabSegment.Tab home = new QMUITabSegment.Tab(
                ContextCompat.getDrawable(mContext, R.mipmap.home_u),
                ContextCompat.getDrawable(mContext, R.mipmap.home_d),
                "首页", false
        );
        QMUITabSegment.Tab find = new QMUITabSegment.Tab(
                ContextCompat.getDrawable(mContext, R.mipmap.found_u),
                ContextCompat.getDrawable(mContext, R.mipmap.found_d),
                "发现", false
        );
        QMUITabSegment.Tab my = new QMUITabSegment.Tab(
                ContextCompat.getDrawable(mContext, R.mipmap.admin_u),
                ContextCompat.getDrawable(mContext, R.mipmap.admin_d),
                "我的", false
        );
        home.setTextColor(0xFF909090, 0xFFFF5A5A);
        find.setTextColor(0xFF909090, 0xFFFF5A5A);
        my.setTextColor(0xFF909090, 0xFFFF5A5A);
        mTabs.addTab(home)
                .addTab(find)
                .addTab(my);
    }

    private void initPagers() {
        mPages = new HashMap<>(3);
        mPages.put(Pager.HOME, new HomeFragment());
        mPages.put(Pager.FIND, new FindFragment());
        mPages.put(Pager.MY, new MyFragment());

        mPagerAdapter = new MyFragPagerAdapter(getSupportFragmentManager(), mPages);

        mPager.setOffscreenPageLimit(3);
        mPager.setAdapter(mPagerAdapter);
        mTabs.setupWithViewPager(mPager, false);

        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        QMUIStatusBarHelper.setStatusBarLightMode(mContext);
                        break;
                    case 1:
                        QMUIStatusBarHelper.setStatusBarLightMode(mContext);
                        break;
                    case 2:
                        QMUIStatusBarHelper.setStatusBarDarkMode(mContext);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    //成功
    @Override
    public void onPermissionsGranted(int requestCode, List<String> list) {
    }

    //失败
    @Override
    public void onPermissionsDenied(int requestCode, List<String> list) {

    }

    private class MyFragPagerAdapter extends FragmentPagerAdapter {

        private HashMap<Pager, Fragment> pagers;

        public MyFragPagerAdapter(FragmentManager fm, HashMap<Pager, Fragment> pagers) {
            super(fm);
            this.pagers = pagers;
        }

        @Override
        public Fragment getItem(int position) {
            return pagers.get(Pager.getPagerFromPositon(position));
        }

        @Override
        public int getCount() {
            return pagers.size();
        }
    }

    enum Pager {
        HOME, FIND, MY;

        public static Pager getPagerFromPositon(int position) {
            switch (position) {
                case 0:
                    return HOME;
                case 1:
                    return FIND;
                case 2:
                    return MY;
                default:
                    return HOME;
            }
        }
    }
}
