package com.sdwfqin.quicklib.dialog;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.StringUtils;
import com.qmuiteam.qmui.skin.QMUISkinManager;
import com.sdwfqin.quicklib.R;
import com.sdwfqin.quicklib.databinding.QuickDialogSimpleHintBinding;

/**
 * 描述：提示弹窗
 *
 * @author 张钦
 * @date 2018/1/16
 */
public class QuickSimpleHintDialog extends DialogFragment implements View.OnClickListener {

    private QuickDialogSimpleHintBinding mBinding;
    private OnDialogClickListener mOnDialogClickListener;
    private Bundle mBundle;

    public QuickSimpleHintDialog(OnDialogClickListener onClickListener, Bundle bundle) {
        mOnDialogClickListener = onClickListener;
        mBundle = bundle;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.transactionDialog);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = QuickDialogSimpleHintBinding.inflate(getLayoutInflater());
        initDataAndEvent();
        return mBinding.getRoot();
    }

    private void initDataAndEvent() {

        if (!StringUtils.isEmpty(mBundle.getCharSequence("titleText"))) {
            mBinding.title.setText(mBundle.getCharSequence("titleText"));
        }
        if (mBundle.getInt("titleTextColor", -1) > 0) {
            mBinding.title.setTextColor(mBundle.getInt("titleTextColor"));
        }
        if (mBundle.getInt("titleBackgroundColor", -1) > 0) {
            mBinding.title.setBackgroundColor(mBundle.getInt("titleBackgroundColor"));
        }
        if (!StringUtils.isEmpty(mBundle.getCharSequence("submitText"))) {
            mBinding.submit.setText(mBundle.getCharSequence("submitText"));
        }
        if (mBundle.getInt("submitTextColor", -1) > 0) {
            mBinding.submit.setTextColor(mBundle.getInt("submitTextColor"));
        }
        if (mBundle.getInt("submitBackgroundColor", -1) > 0) {
            mBinding.submit.setBackgroundColor(mBundle.getInt("submitBackgroundColor"));
        }
        if (!StringUtils.isEmpty(mBundle.getCharSequence("cancelText"))) {
            mBinding.cancel.setText(mBundle.getCharSequence("cancelText"));
        }
        if (mBundle.getInt("cancelTextColor", -1) > 0) {
            mBinding.cancel.setTextColor(mBundle.getInt("cancelTextColor"));
        }
        if (mBundle.getInt("cancelBackgroundColor", -1) > 0) {
            mBinding.cancel.setBackgroundColor(mBundle.getInt("cancelBackgroundColor"));
        }
        if (mBundle.getBoolean("showCancelButton", true)) {
            mBinding.cancel.setVisibility(View.VISIBLE);
        } else {
            mBinding.cancel.setVisibility(View.GONE);
        }

        mBinding.cancel.setOnClickListener(this);
        mBinding.submit.setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        // 设定窗口宽度为屏幕的70%
        int width = (int) (ScreenUtils.getScreenWidth() * 0.7);
        getDialog().getWindow().setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT);
        getDialog().getWindow().setGravity(Gravity.CENTER);
        if (mBundle.getBoolean("followSkin", false)) {
            QMUISkinManager.defaultInstance(getContext()).register(this);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mBundle.getBoolean("followSkin", false)) {
            QMUISkinManager.defaultInstance(getContext()).unRegister(this);
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();

        if (mOnDialogClickListener == null) {
            dismiss();
            return;
        }

        if (i == R.id.cancel) {
            mOnDialogClickListener.cancel(this);
        } else if (i == R.id.submit) {
            mOnDialogClickListener.submit(this);
        }
    }

    public static class Builder {

        private Bundle bundle;
        private OnDialogClickListener onClickListener;

        public Builder() {
            bundle = new Bundle();
        }

        /**
         * 是否支持qmui换肤
         */
        public Builder setFollowSkin(boolean followSkin) {
            bundle.putBoolean("followSkin", followSkin);
            return this;
        }

        public Builder setTitleText(CharSequence titleText) {
            bundle.putCharSequence("titleText", titleText);
            return this;
        }

        public Builder setTitleTextColor(@ColorInt int titleTextColor) {
            bundle.putInt("titleTextColor", titleTextColor);
            return this;
        }

        public Builder setTitleBackgroundColor(@ColorInt int titleBackgroundColor) {
            bundle.putInt("titleBackgroundColor", titleBackgroundColor);
            return this;
        }

        public Builder setSubmitText(CharSequence submitText) {
            bundle.putCharSequence("submitText", submitText);
            return this;
        }

        public Builder setSubmitTextColor(@ColorInt int submitTextColor) {
            bundle.putInt("submitTextColor", submitTextColor);
            return this;
        }

        public Builder setSubmitBackgroundColor(@ColorInt int submitBackgroundColor) {
            bundle.putInt("submitBackgroundColor", submitBackgroundColor);
            return this;
        }

        public Builder setCancelText(CharSequence cancelText) {
            bundle.putCharSequence("cancelText", cancelText);
            return this;
        }

        public Builder setCancelTextColor(@ColorInt int cancelTextColor) {
            bundle.putInt("cancelTextColor", cancelTextColor);
            return this;
        }

        public Builder setCancelBackgroundColor(@ColorInt int cancelBackgroundColor) {
            bundle.putInt("cancelBackgroundColor", cancelBackgroundColor);
            return this;
        }

        /**
         * 是否显示取消按钮
         */
        public Builder setShowCancelButton(boolean showCancelButton) {
            bundle.putBoolean("showCancelButton", showCancelButton);
            return this;
        }

        public Builder setOnClickListener(OnDialogClickListener onClickListener) {
            this.onClickListener = onClickListener;
            return this;
        }

        public QuickSimpleHintDialog builder() {
            return new QuickSimpleHintDialog(onClickListener, bundle);
        }
    }

    /**
     * 按钮监听接口
     */
    public interface OnDialogClickListener {

        /**
         * 提交
         */
        void submit(QuickSimpleHintDialog dialog);

        /**
         * 关闭
         */
        default void cancel(QuickSimpleHintDialog dialog) {

        }
    }
}
