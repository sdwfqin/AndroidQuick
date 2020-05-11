package com.sdwfqin.quickseed.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.sdwfqin.quicklib.base.BaseFragment;
import com.sdwfqin.quicklib.dialog.HintDialog;
import com.sdwfqin.quicklib.imagepreview.ImagePreviewActivity;
import com.sdwfqin.quicklib.webview.WebViewActivity;
import com.sdwfqin.quickseed.R;
import com.sdwfqin.quickseed.base.ArouterConstants;
import com.sdwfqin.quickseed.databinding.FragmentMainBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：主Activity
 *
 * @author 张钦
 */
@Route(path = ArouterConstants.MAIN_HOME)
public class MainFragment extends BaseFragment<FragmentMainBinding> {

    private String[] mTitle = new String[]{
            "图片预览",
            "上传图片九宫格",
            "自定义验证码/密码View",
            "跑马灯Demo",
            "Camerax（支持二维码识别）",
            "VLayoutDemo",
            "展示SVG图片",
            "跳转网页",
            "自定义Webview",
            "悬浮窗与截图",
            "弹窗",
            "Mvvm Demo",
            "Mvp Demo",
            "圆（方）形加载进度条",
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
                    strings.add("https://sdwfqin1-1252249614.cos.ap-beijing-1.myqcloud.com/blog/service_v1.0.png");
                    strings.add("https://sdwfqin1-1252249614.costj.myqcloud.com/blog/shopping.gif");
                    ImagePreviewActivity.launch(strings);
                    break;
                case 1:
                    ARouter.getInstance().build(ArouterConstants.COMPONENTS_PICTUREUPLOAD).navigation();
                    break;
                case 2:
                    ARouter.getInstance().build(ArouterConstants.COMPONENTS_PAYPWD).navigation();
                    break;
                case 3:
                    ARouter.getInstance().build(ArouterConstants.COMPONENTS_AUTOPOLLRECYCLER).navigation();
                    break;
                case 4:
                    ARouter.getInstance().build(ArouterConstants.COMPONENTS_CAMERAX).navigation();
                    break;
                case 5:
                    ARouter.getInstance().build(ArouterConstants.COMPONENTS_VLAYOUTSAMPLE).navigation();
                    break;
                case 6:
                    ARouter.getInstance().build(ArouterConstants.COMPONENTS_SHOWSVG).navigation();
                    break;
                case 7:
                    WebViewActivity.launch("https://www.baidu.com");
                    break;
                case 8:
                    ARouter.getInstance().build(ArouterConstants.COMPONENTS_CUSTOMWEBVIEW).navigation();
                    break;
                case 9:
                    ARouter.getInstance().build(ArouterConstants.COMPONENTS_WINDOWFLOATANDSCREENSHOT).navigation();
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
                case 11:
                    ARouter.getInstance().build(ArouterConstants.COMPONENTS_MVVM).navigation();
                    break;
                case 12:
                    ARouter.getInstance().build(ArouterConstants.COMPONENTS_MVP).navigation();
                    break;
                case 13:
                    ARouter.getInstance().build(ArouterConstants.COMPONENTS_CIRCLEPROGRESSDEMO).navigation();
                    break;
                default:
            }
        });
    }
}
