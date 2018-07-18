package com.sdwfqin.quickseed.ui;

import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.StringUtils;
import com.sdwfqin.qrscan.QrCreateTool;
import com.sdwfqin.quicklib.base.BaseActivity;
import com.sdwfqin.quickseed.R;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 描述：生成二维码
 *
 * @author zhangqin
 * @date 2018/6/25
 */
public class CreateQrActivity extends BaseActivity {

    @BindView(R.id.qr_img)
    ImageView mQrImg;
    @BindView(R.id.content)
    EditText mContent;
    @BindView(R.id.create)
    TextView mCreate;

    @Override
    protected int getLayout() {
        return R.layout.activity_qr_create;
    }

    @Override
    protected void initEventAndData() {

        mTopBar.setTitle("生成二维码");
        mTopBar.addLeftBackImageButton().setOnClickListener(v -> finish());

        // 生成二维码
        QrCreateTool
                .builder("http://www.sdwfqin.com")
                .codeSide(ConvertUtils.dp2px(300))
                .into(mQrImg);
    }

    @OnClick(R.id.create)
    public void onViewClicked() {
        String content = mContent.getText().toString();
        if (StringUtils.isEmpty(content)) {
            showMsg("请输入内容！");
        } else {
            QrCreateTool
                    .builder(content)
                    .codeSide(ConvertUtils.dp2px(300))
                    .into(mQrImg);
        }
    }
}
