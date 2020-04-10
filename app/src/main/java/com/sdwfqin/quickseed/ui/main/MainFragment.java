package com.sdwfqin.quickseed.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.sdwfqin.quicklib.base.BaseFragment;
import com.sdwfqin.quicklib.dialog.HintDialog;
import com.sdwfqin.quicklib.imagepreview.ImagePreviewActivity;
import com.sdwfqin.quicklib.webview.WebViewActivity;
import com.sdwfqin.quickseed.R;
import com.sdwfqin.quickseed.databinding.FragmentMainBinding;
import com.sdwfqin.quickseed.ui.components.AutoPollRecyclerViewActivity;
import com.sdwfqin.quickseed.ui.components.CameraXDemoActivity;
import com.sdwfqin.quickseed.ui.components.CustomWebviewActivity;
import com.sdwfqin.quickseed.ui.components.PayPwdInputActivity;
import com.sdwfqin.quickseed.ui.components.PictureUploadActivity;
import com.sdwfqin.quickseed.ui.components.ShowSvgActivity;
import com.sdwfqin.quickseed.ui.components.VLayoutSampleActivity;
import com.sdwfqin.quickseed.ui.components.WindowFloatAndScreenshotActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：主Activity
 *
 * @author 张钦
 */
public class MainFragment extends BaseFragment<FragmentMainBinding> {

    private String[] mTitle = new String[]{
            "图片预览",
            "上传图片九宫格",
            "自定义验证码/密码View",
            "跑马灯Demo",
            "Camerax",
            "VLayoutDemo",
            "展示SVG图片",
            "跳转网页",
            "自定义Webview",
            "悬浮窗与截图",
            "弹窗",
    };

    @Override
    protected FragmentMainBinding getViewBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return FragmentMainBinding.inflate(inflater);
    }

    @Override
    protected void initEventAndData() {

        mBinding.topBar.setTitle("组件");

        mBinding.list.setAdapter(new ArrayAdapter<>(mContext, R.layout.item_list, R.id.tv_items, mTitle));
    }

    @Override
    protected void initClickListener() {
        mBinding.list.setOnItemClickListener((adapterView, view, i, l) -> {
            switch (i) {
                case 0:
                    List<String> strings = new ArrayList<>();
                    strings.add("https://gss2.bdstatic.com/-fo3dSag_xI4khGkpoWK1HF6hhy/baike/c0%3Dbaike116%2C5%2C5%2C116%2C38/sign=80a0e826da160924c828aa49b56e5e9f/f636afc379310a5585445184bd4543a982261059.jpg");
                    strings.add("https://gss3.bdstatic.com/-Po3dSag_xI4khGkpoWK1HF6hhy/baike/c0%3Dbaike116%2C5%2C5%2C116%2C38/sign=b214b363d754564ef168ec6bd2b7f7e7/7e3e6709c93d70cfc087257df2dcd100baa12b45.jpg");
                    ImagePreviewActivity.launch(mContext, strings);
                    break;
                case 1:
                    startActivity(new Intent(mContext, PictureUploadActivity.class));
                    break;
                case 2:
                    startActivity(new Intent(mContext, PayPwdInputActivity.class));
                    break;
                case 3:
                    startActivity(new Intent(mContext, AutoPollRecyclerViewActivity.class));
                    break;
                case 4:
                    startActivity(new Intent(mContext, CameraXDemoActivity.class));
                    break;
                case 5:
                    startActivity(new Intent(mContext, VLayoutSampleActivity.class));
                    break;
                case 6:
                    startActivity(new Intent(mContext, ShowSvgActivity.class));
                    break;
                case 7:
                    WebViewActivity.launch(mContext, "https://www.baidu.com");
                    break;
                case 8:
                    startActivity(new Intent(mContext, CustomWebviewActivity.class));
                    break;
                case 9:
                    startActivity(new Intent(mContext, WindowFloatAndScreenshotActivity.class));
                    break;
                case 10:
                    HintDialog hintDialog = new HintDialog(mContext);
                    hintDialog.setFollowSkin(true);
                    hintDialog.show();
                    hintDialog.setTitle("热更新测试33333");
                    hintDialog.hideRight();
                    hintDialog.setLeftText("取消");
                    hintDialog.setOnClickListener(new HintDialog.OnDialogClickListener() {
                        @Override
                        public void left() {
                            showMsg("您点击了取消！");
                        }

                        @Override
                        public void right() {

                        }
                    });
                    break;
                default:
            }
        });
    }

    @Override
    protected void lazyLoadShow() {

    }
}
