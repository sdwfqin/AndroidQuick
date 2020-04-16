package com.sdwfqin.quickseed.ui.components;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.sdwfqin.quickseed.base.ArouterConstants;
import com.sdwfqin.quickseed.base.SampleBaseActivity;
import com.sdwfqin.quickseed.databinding.ActivityPayPwdInputBinding;

/**
 * 描述：自定义输入密码或验证码View
 *
 * @author zhangqin
 * @date 2018/6/12
 */
@Route(path = ArouterConstants.COMPONENTS_PAYPWD)
public class PayPwdInputActivity extends SampleBaseActivity<ActivityPayPwdInputBinding> {

    @Override
    protected ActivityPayPwdInputBinding getViewBinding() {
        return ActivityPayPwdInputBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void initEventAndData() {
        mTopBar.setTitle("自定义输入密码或验证码View");
        mTopBar.addLeftBackImageButton().setOnClickListener(v -> finish());
    }

    @Override
    protected void initClickListener() {

    }
}
