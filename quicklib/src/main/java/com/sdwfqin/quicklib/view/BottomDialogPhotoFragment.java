package com.sdwfqin.quicklib.view;

import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.View;
import android.widget.TextView;

import com.sdwfqin.quicklib.R;

/**
 * 描述：修改头像弹窗
 *
 * @author 张钦
 * @date 2018/1/16
 */
public class BottomDialogPhotoFragment extends BottomSheetDialogFragment implements View.OnClickListener {

    private OnDialogClickListener mOnDialogClickListener;
    private TextView mXaingce;
    private TextView mPaizhao;
    private TextView mExit;

    private boolean HideView = false;

    /**
     * 按钮监听接口
     */
    public interface OnDialogClickListener {

        /**
         * 相册
         */
        void xiangce();

        /**
         * 拍照
         */
        void paizhao();

        /**
         * 取消
         */
        void exit();
    }

    /**
     * 设置监听
     *
     * @param onDialogClickListener
     */
    public void setOnClickListener(OnDialogClickListener onDialogClickListener) {
        mOnDialogClickListener = onDialogClickListener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        View view = View.inflate(getContext(), R.layout.quick_bottom_photo, null);
        mXaingce = view.findViewById(R.id.xiangce);
        mPaizhao = view.findViewById(R.id.paizhao);
        mExit = view.findViewById(R.id.exit);

        mXaingce.setOnClickListener(this);
        mPaizhao.setOnClickListener(this);
        mExit.setOnClickListener(this);

        dialog.setContentView(view);

        dialog.getWindow().findViewById(R.id.design_bottom_sheet)
                // #00ffffff
                .setBackgroundResource(R.color.transparent);

        return dialog;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.xiangce) {
            mOnDialogClickListener.xiangce();
            dismiss();
        } else if (i == R.id.paizhao) {
            mOnDialogClickListener.paizhao();
            dismiss();
        } else if (i == R.id.exit) {
            mOnDialogClickListener.exit();
            dismiss();
        }
    }

    public static class Builder {

        private BottomDialogPhotoFragment mDialogFragment;

        public Builder() {
            mDialogFragment = new BottomDialogPhotoFragment();
        }

        public Builder setOnClickListener(OnDialogClickListener onClickListener) {
            mDialogFragment.mOnDialogClickListener = onClickListener;
            return this;
        }

        public BottomSheetDialogFragment builder() {
            return mDialogFragment;
        }
    }
}
