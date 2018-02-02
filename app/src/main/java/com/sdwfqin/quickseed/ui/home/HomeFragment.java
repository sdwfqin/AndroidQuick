package com.sdwfqin.quickseed.ui.home;

import android.content.Intent;
import android.os.Build;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sdwfqin.quicklib.base.BaseFragment;
import com.sdwfqin.quicklib.module.WebViewActivity;
import com.sdwfqin.quicklib.module.qrbarscan.QrBarScanActivity;
import com.sdwfqin.quicklib.view.dialog.HintDialog;
import com.sdwfqin.quickseed.R;
import com.sdwfqin.quickseed.base.Constants;

import butterknife.BindView;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

/**
 * 描述：首页
 *
 * @author 张钦
 * @date 2018/1/15
 */
public class HomeFragment extends BaseFragment {

    @BindView(R.id.status_view)
    View mStatusView;
    @BindView(R.id.home_msg)
    ImageView mHomeMsg;
    @BindView(R.id.home_title_tv)
    TextView mHomeTitleTv;

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

        mHomeTitleTv.setText("首页");

    }

    @Override
    protected void lazyLoad() {

    }

    @OnClick({R.id.a, R.id.b, R.id.c})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.a:
                WebViewActivity.launch(mContext, "https://www.baidu.com");
                break;
            case R.id.b:
                startActivityForResult(new Intent(mContext, QrBarScanActivity.class), Constants.RESULT_CODE_1);
                break;
            case R.id.c:
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
        }
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
            }
        }
    }
}
