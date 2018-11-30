package com.sdwfqin.quickseed.ui;

import android.Manifest;
import android.content.Intent;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.FragmentManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.sdwfqin.quicklib.base.BaseActivity;
import com.sdwfqin.quicklib.module.seeimage.SeeImageActivity;
import com.sdwfqin.quicklib.module.webview.WebViewActivity;
import com.sdwfqin.quicklib.view.BottomDialogPhotoFragment;
import com.sdwfqin.quicklib.view.HintDialog;
import com.sdwfqin.quickseed.R;
import com.sdwfqin.quickseed.base.Constants;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 描述：主Activity
 *
 * @author 张钦
 */
public class MainActivity extends BaseActivity {

    @BindView(R.id.list)
    ListView list;

    private String[] mTitle = new String[]{"跳转网页",
            "颤抖的按钮",
            "图片预览",
            "底部弹窗",
            "上传图片九宫格",
            "自定义验证码/密码View",
            "自定义Webview",
    };

    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initEventAndData() {

        Constants.STATUS_HEIGHT = QMUIStatusBarHelper.getStatusbarHeight(mContext);

        getPermissions();

        mTopBar.setTitle("首页");

        list.setAdapter(new ArrayAdapter<>(mContext, R.layout.item_list, R.id.tv_items, mTitle));
        initListener();
    }

    private void getPermissions() {

        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
        checkPermissions(perms, true, true, new OnPermissionCallback() {
            @Override
            public void onSuccess() {
                Toast.makeText(mContext, "取得权限", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onError() {
                Toast.makeText(mContext, "没有取得权限", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean isStartSwipeBack() {
        return false;
    }

    private void initListener() {
        list.setOnItemClickListener((adapterView, view, i, l) -> {
            switch (i) {
                case 0:
                    WebViewActivity.launch(mContext, "https://www.baidu.com");
                    break;
                case 1:
                    HintDialog hintDialog = new HintDialog(mContext);
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
                case 2:
                    List<String> strings = new ArrayList<>();
                    strings.add("http://pic4.nipic.com/20091217/3885730_124701000519_2.jpg");
                    strings.add("http://img.taopic.com/uploads/allimg/140729/240450-140HZP45790.jpg");
                    SeeImageActivity.launch(mContext, strings);
                    break;
                case 3:
                    BottomDialogPhotoFragment.Builder builder = new BottomDialogPhotoFragment.Builder();
                    BottomSheetDialogFragment bottomSheetDialogFragment = builder.setOnClickListener(new BottomDialogPhotoFragment.OnDialogClickListener() {
                        @Override
                        public void xiangce() {

                        }

                        @Override
                        public void paizhao() {

                        }

                        @Override
                        public void exit() {

                        }
                    }).builder();
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    if (fragmentManager != null) {
                        bottomSheetDialogFragment.show(fragmentManager, "dialog");
                    }
                    break;
                case 4:
                    startActivity(new Intent(mContext, PictureUploadActivity.class));
                    break;
                case 5:
                    startActivity(new Intent(mContext, PayPwdInputActivity.class));
                    break;
                case 6:
                    startActivity(new Intent(mContext, CustomWebviewActivity.class));
                    break;
                default:
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Constants.RESULT_CODE_1:
                    String code = data.getStringExtra("data");
                    showMsg(code);
                    break;
                default:
            }
        }
    }
}
