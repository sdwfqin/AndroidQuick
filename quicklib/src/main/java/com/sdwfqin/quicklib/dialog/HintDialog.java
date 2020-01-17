package com.sdwfqin.quicklib.dialog;

import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialog;
import androidx.cardview.widget.CardView;

import com.blankj.utilcode.util.LogUtils;
import com.qmuiteam.qmui.skin.QMUISkinManager;
import com.sdwfqin.quicklib.R;

/**
 * 描述：提示弹窗
 *
 * @author 张钦
 * @date 2018/1/16
 */
public class HintDialog extends AppCompatDialog implements View.OnClickListener {

    CardView mLayout;
    TextView mDialogTitle;
    TextView mLeft;
    TextView mRight;

    private Context mContext;
    private OnDialogClickListener mOnDialogClickListener;
    private boolean mFollowSkin = false;

    public HintDialog(@NonNull Context context) {
        super(context, R.style.transactionDialog);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.quick_dialog_hint);

        mLayout = findViewById(R.id.root_view);
        mDialogTitle = findViewById(R.id.title);
        mLeft = findViewById(R.id.left);
        mRight = findViewById(R.id.right);

        // 设定窗口宽度为屏幕的70%
        DisplayMetrics dm = new DisplayMetrics();
        getWindow().getWindowManager().getDefaultDisplay().getMetrics(dm);
        mLayout.getLayoutParams().width = (int) ((dm.widthPixels) * 0.7);

        mLeft.setOnClickListener(this);
        mRight.setOnClickListener(this);
    }

    public void setFollowSkin(boolean followSkin) {
        mFollowSkin = followSkin;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mFollowSkin) {
            QMUISkinManager.defaultInstance(getContext()).register(this);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        QMUISkinManager.defaultInstance(getContext()).unRegister(this);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();

        if (mOnDialogClickListener == null) {
            dismiss();
            return;
        }

        if (i == R.id.left) {
            mOnDialogClickListener.left();
            dismiss();

        } else if (i == R.id.right) {
            mOnDialogClickListener.right();
            dismiss();

        }
    }

    /**
     * 按钮监听接口
     */
    public interface OnDialogClickListener {

        /**
         * 提交
         */
        void left();

        /**
         * 关闭
         */
        void right();
    }

    /**
     * 设置监听
     *
     * @param onDialogClickListener
     */
    public void setOnClickListener(OnDialogClickListener onDialogClickListener) {
        mOnDialogClickListener = onDialogClickListener;
    }

    /**
     * 设置标题
     *
     * @param title
     */
    public void setTitle(String title) {
        try {
            mDialogTitle.setText(title);
        } catch (Exception e) {
            LogUtils.e("setTitle: ", e);
        }
    }

    /**
     * 设置标题文字颜色
     *
     * @param titleColor
     */
    public void setTitleColor(@ColorInt int titleColor) {
        try {
            mDialogTitle.setTextColor(titleColor);
        } catch (Exception e) {
            LogUtils.e("setTitleColor: ", e);
        }
    }

    /**
     * 设置右侧按钮文字
     *
     * @param rightText
     */
    public void setRightText(String rightText) {
        try {
            mRight.setText(rightText);
        } catch (Exception e) {
            LogUtils.e("setRightText: ", e);
        }
    }

    /**
     * 设置右侧按钮背景色
     *
     * @param rightBgColor
     */
    public void setRightBgColor(@ColorInt int rightBgColor) {
        try {
            mRight.setBackgroundColor(rightBgColor);
        } catch (Exception e) {
            LogUtils.e("setRightBgColor: ", e);
        }
    }

    /**
     * 设置左侧按钮文字
     *
     * @param leftText
     */
    public void setLeftText(String leftText) {
        try {
            mLeft.setText(leftText);
        } catch (Exception e) {
            LogUtils.e("setLeftText: ", e);
        }
    }

    /**
     * 设置左侧按钮背景色
     *
     * @param leftBgColor
     */
    public void setLeftBgColor(@ColorInt int leftBgColor) {
        try {
            mLeft.setBackgroundColor(leftBgColor);
        } catch (Exception e) {
            LogUtils.e("setLeftBgColor: ", e);
        }
    }

    /**
     * 隐藏右侧按钮
     */
    public void hideRight() {
        try {
            mRight.setVisibility(View.GONE);
        } catch (Exception e) {
            LogUtils.e("hideRight: ", e);
        }
    }
}
