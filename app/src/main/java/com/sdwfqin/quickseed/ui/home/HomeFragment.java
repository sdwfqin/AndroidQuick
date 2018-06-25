package com.sdwfqin.quickseed.ui.home;

import android.content.Intent;
import android.os.Build;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.qmuiteam.qmui.widget.QMUITopBar;
import com.sdwfqin.qrscan.QrBarScanActivity;
import com.sdwfqin.quicklib.base.BaseFragment;
import com.sdwfqin.quicklib.module.seeimage.SeeImageActivity;
import com.sdwfqin.quicklib.module.webview.WebViewActivity;
import com.sdwfqin.quicklib.view.BottomDialogPhotoFragment;
import com.sdwfqin.quicklib.view.HintDialog;
import com.sdwfqin.quickseed.R;
import com.sdwfqin.quickseed.base.Constants;
import com.sdwfqin.quickseed.ui.PictureUploadActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static android.app.Activity.RESULT_OK;

/**
 * 描述：首页
 *
 * @author 张钦
 * @date 2018/1/15
 */
public class HomeFragment extends BaseFragment {

    @BindView(R.id.list)
    ListView list;
    @BindView(R.id.status_view)
    View mStatusView;
    @BindView(R.id.topbar)
    QMUITopBar mTopbar;

    private String[] mTitle = new String[]{"跳转网页",
            "扫描二维码",
            "颤抖的按钮",
            "图片预览",
            "底部弹窗",
            "上传图片九宫格",
            "自定义验证码/密码View",
            "自定义Webview",
            "屏幕适配测试",
    };

    @Override
    protected int getLayout() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initEventAndData() {

        if (Build.VERSION.SDK_INT < 19) {
            mStatusView.setVisibility(View.GONE);
        } else {
            mStatusView.getLayoutParams().height = Constants.STATUS_HEIGHT;
        }

        mTopbar.setTitle("首页");

        list.setAdapter(new ArrayAdapter<>(mContext, R.layout.item_list, R.id.tv_items, mTitle));
        initListener();
    }

    private void initListener() {
        list.setOnItemClickListener((adapterView, view, i, l) -> {
            switch (i) {
                case 0:
                    WebViewActivity.launch(mContext, "https://www.baidu.com");
                    break;
                case 1:
                    startActivityForResult(new Intent(mContext, QrBarScanActivity.class), Constants.RESULT_CODE_1);
                    break;
                case 2:
                    HintDialog hintDialog = new HintDialog(mContext);
                    hintDialog.show();
                    hintDialog.setTitle("啊啊啊啊啊啊");
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
                case 3:
                    List<String> strings = new ArrayList<>();
                    strings.add("http://pic4.nipic.com/20091217/3885730_124701000519_2.jpg");
                    strings.add("http://img.taopic.com/uploads/allimg/140729/240450-140HZP45790.jpg");
                    SeeImageActivity.launch(mContext, strings);
                    break;
                case 4:
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
                    FragmentManager fragmentManager = getFragmentManager();
                    if (fragmentManager != null) {
                        bottomSheetDialogFragment.show(fragmentManager, "dialog");
                    }
                    break;
                case 5:
                    startActivity(new Intent(mContext, PictureUploadActivity.class));
                    break;
                case 6:
                    startActivity(new Intent(mContext, PayPwdInputActivity.class));
                    break;
                case 7:
                    startActivity(new Intent(mContext, CustomWebviewActivity.class));
                    break;
                case 8:
                    startActivity(new Intent(mContext, DensityActivity.class));
                    break;
                default:
            }
        });
    }

    @Override
    protected void lazyLoadShow(boolean isLoad) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Constants.RESULT_CODE_1:
                    String code = data.getStringExtra("data");
                    if (code != null) {
                        showMsg(code);
                    }
                    break;
                default:
            }
        }
    }
}
