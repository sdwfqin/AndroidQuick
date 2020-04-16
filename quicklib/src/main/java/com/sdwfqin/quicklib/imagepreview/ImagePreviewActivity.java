package com.sdwfqin.quicklib.imagepreview;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.sdwfqin.quicklib.R;
import com.sdwfqin.quicklib.base.BaseActivity;
import com.sdwfqin.quicklib.base.QuickArouterConstants;
import com.sdwfqin.quicklib.databinding.QuickActivityImagePreviewBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * 图片查看与保存Activity
 * <p>
 * 需传入string类型的集合
 *
 * @author zhangqin
 * @date 2017/8/7
 */
@Route(path = QuickArouterConstants.QUICK_IMAGEPREVIEW)
public class ImagePreviewActivity extends BaseActivity<QuickActivityImagePreviewBinding> {

    private List<String> mImageList;
    private int position;

    private ShowImagePagerAdapter mShowImagePagerAdapter;

    /**
     * 图片预览
     *
     * @param stringList 图片链接
     * @return
     */
    public static void launch(@Nullable List<String> stringList) {
        launch(stringList, 0);
    }

    /**
     * @param stringList
     * @param position   当前图片位置
     * @return
     */
    public static void launch(@Nullable List<String> stringList, int position) {
        ARouter
                .getInstance()
                .build(QuickArouterConstants.QUICK_IMAGEPREVIEW)
                .withStringArrayList("data", (ArrayList<String>) stringList)
                .withInt("position", position)
                .navigation();
    }

    @Override
    protected QuickActivityImagePreviewBinding getViewBinding() {
        return QuickActivityImagePreviewBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void initEventAndData() {

        QMUIStatusBarHelper.setStatusBarDarkMode(mContext);
        mTopBar.setVisibility(View.GONE);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mImageList = this.getIntent().getStringArrayListExtra("data");
            position = this.getIntent().getIntExtra("position", 0);
        } else {
            showMsg(getString(R.string.quick_args_get_error));
            finish();
        }

        mShowImagePagerAdapter = new ShowImagePagerAdapter(getSupportFragmentManager(), getLifecycle());
        mBinding.viewpager.setAdapter(mShowImagePagerAdapter);
        mBinding.viewpager.setCurrentItem(position);

        if (mImageList.size() == 1) {
            mBinding.position.setVisibility(View.GONE);
        } else {
            mBinding.position.setVisibility(View.VISIBLE);
        }

        setPosText(position + 1, mImageList.size());

        mBinding.viewpager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                setPosText(position + 1, mImageList.size());
            }
        });
    }

    @Override
    protected void initClickListener() {

    }

    /**
     * 指示器
     *
     * @param position
     * @param size
     */
    @SuppressLint("SetTextI18n")
    public void setPosText(int position, int size) {
        mBinding.position.setText(position + "/" + size);
    }

    private class ShowImagePagerAdapter extends FragmentStateAdapter {

        public ShowImagePagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
            super(fragmentManager, lifecycle);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return ImagePreviewFragment.newInstance(mImageList.get(position));
        }

        @Override
        public int getItemCount() {
            return mImageList.size();
        }
    }
}
