package com.sdwfqin.quickseed.ui;

import com.sdwfqin.quicklib.base.BaseActivity;
import com.sdwfqin.quickseed.R;

/**
 * 描述：自定义输入密码或验证码View
 *
 * @author zhangqin
 * @date 2018/6/12
 */
public class PayPwdInputActivity extends BaseActivity {

    @Override
    protected int getLayout() {
        return R.layout.activity_pay_pwd_input;
    }

    @Override
    protected void initEventAndData() {
        mTopBar.setTitle("自定义输入密码或验证码View");
        mTopBar.addLeftBackImageButton().setOnClickListener(v -> finish());
    }
}
