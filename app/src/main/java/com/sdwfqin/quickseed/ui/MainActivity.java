package com.sdwfqin.quickseed.ui;

import android.Manifest;
import android.content.Intent;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.sdwfqin.quicklib.base.BaseActivity;
import com.sdwfqin.quicklib.dialog.HintDialog;
import com.sdwfqin.quicklib.imagepreview.ImagePreviewActivity;
import com.sdwfqin.quicklib.webview.WebViewActivity;
import com.sdwfqin.quickseed.R;
import com.sdwfqin.quickseed.base.Constants;

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
    ListView mList;

    private String[] mTitle = new String[]{
            "跳转网页",
            "弹窗",
            "图片预览",
            "上传图片九宫格",
            "自定义验证码/密码View",
            "自定义Webview",
            "跑马灯Demo",
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

        mList.setAdapter(new ArrayAdapter<>(mContext, R.layout.item_list, R.id.tv_items, mTitle));
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

    private void initListener() {
        mList.setOnItemClickListener((adapterView, view, i, l) -> {
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
                    strings.add("http://sdwfqin1-1252249614.cos.ap-beijing-1.myqcloud.com/blog/MicroText%20(1).png");
                    strings.add("http://sdwfqin1-1252249614.cos.ap-beijing-1.myqcloud.com/blog/MicroText%20(4).png");
                    ImagePreviewActivity.launch(mContext, strings);
                    break;
                case 3:
                    startActivity(new Intent(mContext, PictureUploadActivity.class));
                    break;
                case 4:
                    startActivity(new Intent(mContext, PayPwdInputActivity.class));
                    break;
                case 5:
                    startActivity(new Intent(mContext, CustomWebviewActivity.class));
                    break;
                case 6:
                    startActivity(new Intent(mContext, AutoPollRecyclerViewActivity.class));
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
