package com.sdwfqin.quickseed.ui;

import android.widget.ImageView;

import com.blankj.utilcode.util.ConvertUtils;
import com.sdwfqin.qrscan.QrCreateTool;
import com.sdwfqin.quicklib.base.BaseActivity;
import com.sdwfqin.quickseed.R;

import butterknife.BindView;

/**
 * 描述：生成二维码
 *
 * @author zhangqin
 * @date 2018/6/25
 */
public class CreateQrActivity extends BaseActivity {

    @BindView(R.id.qr_img)
    ImageView mQrImg;

    @Override
    protected int getLayout() {
        return R.layout.activity_qr_create;
    }

    @Override
    protected void initEventAndData() {
        // 生成二维码
        QrCreateTool
                .builder("二维图热狗热狗特人")
                .codeSide(ConvertUtils.dp2px(300))
                .into(mQrImg);
    }
}
