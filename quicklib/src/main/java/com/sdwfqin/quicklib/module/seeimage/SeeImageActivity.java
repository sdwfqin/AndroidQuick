package com.sdwfqin.quicklib.module.seeimage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
public class SeeImageActivity extends BaseActivity {

    private ViewPager mViewPager;
    private TextView mPosition;
    protected Button mSave;

    private List<String> mMainList;
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

        Intent intent = new Intent(context, SeeImageActivity.class);
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

        Intent intent = new Intent(context, SeeImageActivity.class);
        intent.putStringArrayListExtra("data", (ArrayList<String>) stringList);
        intent.putExtra("position", position);
        context.startActivity(intent);
    }

    @Override
    protected int getLayout() {
        return R.layout.quick_activity_see_image;
    }

    @Override
    protected void initEventAndData() {

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mMainList = this.getIntent().getStringArrayListExtra("data");
            position = this.getIntent().getIntExtra("position", 0);
        } else {
            showMsg("参数获取失败");
            finish();
        }

        mViewPager = findViewById(R.id.viewpager);
        mPosition = findViewById(R.id.position);

        mTopBar.setTitle("查看图片");
        mTopBar.addLeftBackImageButton().setOnClickListener(v -> finish());
        mSave = mTopBar.addRightTextButton("保存", R.id.add);

        mShowImagePagerAdapter = new ShowImagePagerAdapter();
        mViewPager.setAdapter(mShowImagePagerAdapter);
        mViewPager.setCurrentItem(position);

        if (mMainList.size() == 1) {
            mPosition.setVisibility(View.GONE);
        } else {
            mPosition.setVisibility(View.VISIBLE);
        }

        setTextSize(position + 1, mMainList.size());

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setTextSize(position + 1, mMainList.size());
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
    public void setTextSize(int position, int size) {
        mPosition.setText(position + "/" + size);
    }

    private class ShowImagePagerAdapter extends FragmentStatePagerAdapter {

        public ShowImagePagerAdapter() {
            super(getSupportFragmentManager());
        }

        @Override
        public int getCount() {
            return mMainList.size();
        }

        @Override
        public Fragment getItem(int position) {
            return SeeImageFragment.newInstance(mMainList.get(position), position);
        }

    }
}
