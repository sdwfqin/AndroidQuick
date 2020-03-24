package com.sdwfqin.quicklib.imagepreview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.sdwfqin.quicklib.R;
import com.sdwfqin.quicklib.base.BaseActivity;

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
public class ImagePreviewActivity extends BaseActivity {

    private ViewPager mViewPager;
    private TextView mPosition;

    private List<String> mImageList;
    private int position;

    private ShowImagePagerAdapter mShowImagePagerAdapter;

    /**
     * 图片预览
     *
     * @param context
     * @param stringList 图片链接
     * @return
     */
    public static void launch(@NonNull Context context, @Nullable List<String> stringList) {

        Intent intent = new Intent(context, ImagePreviewActivity.class);
        intent.putStringArrayListExtra("data", (ArrayList<String>) stringList);
        context.startActivity(intent);
    }

    /**
     * @param context
     * @param stringList
     * @param position   当前图片位置
     * @return
     */
    public static void launch(@NonNull Context context, @Nullable List<String> stringList, int position) {

        Intent intent = new Intent(context, ImagePreviewActivity.class);
        intent.putStringArrayListExtra("data", (ArrayList<String>) stringList);
        intent.putExtra("position", position);
        context.startActivity(intent);
    }

    @Override
    protected int getLayout() {
        return R.layout.quick_activity_image_preview;
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

        mViewPager = findViewById(R.id.viewpager);
        mPosition = findViewById(R.id.position);

        mShowImagePagerAdapter = new ShowImagePagerAdapter();
        mViewPager.setAdapter(mShowImagePagerAdapter);
        mViewPager.setCurrentItem(position);

        if (mImageList.size() == 1) {
            mPosition.setVisibility(View.GONE);
        } else {
            mPosition.setVisibility(View.VISIBLE);
        }

        setPosText(position + 1, mImageList.size());

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setPosText(position + 1, mImageList.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * 指示器
     *
     * @param position
     * @param size
     */
    @SuppressLint("SetTextI18n")
    public void setPosText(int position, int size) {
        mPosition.setText(position + "/" + size);
    }

    private class ShowImagePagerAdapter extends FragmentStatePagerAdapter {

        public ShowImagePagerAdapter() {
            super(getSupportFragmentManager());
        }

        @Override
        public int getCount() {
            return mImageList.size();
        }

        @Override
        public Fragment getItem(int position) {
            return ImagePreviewFragment.newInstance(mImageList.get(position));
        }

    }
}
