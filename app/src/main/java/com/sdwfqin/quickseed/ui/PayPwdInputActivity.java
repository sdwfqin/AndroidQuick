package com.sdwfqin.quickseed.ui;

import com.qmuiteam.qmui.widget.QMUITopBar;
import com.sdwfqin.quicklib.base.BaseActivity;
import com.sdwfqin.quickseed.R;

import butterknife.BindView;

/**
 * 描述：自定义输入密码或验证码View
 *
 * @author zhangqin
 * @date 2018/6/12
 */
public class PayPwdInputActivity extends BaseActivity {

    @BindView(R.id.topbar)
    QMUITopBar mTopbar;

    @Override
    protected int getLayout() {
        return R.layout.activity_pay_pwd_input;
    }

    @Override
    protected void initEventAndData() {
        mTopbar.setTitle("自定义输入密码或验证码View");
        mTopbar.addLeftBackImageButton().setOnClickListener(v -> finish());
    }
}
