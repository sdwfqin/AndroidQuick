package com.sdwfqin.quicklib.imagepreview;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.sdwfqin.quicklib.R;
import com.sdwfqin.quicklib.databinding.QuickBottomImagePreviewBinding;

/**
 * 描述：保存图片弹窗
 *
 * @author 张钦
 * @date 2018/1/16
 */
public class BottomDialogImagePreviewFragment extends BottomSheetDialogFragment implements View.OnClickListener {

    private OnDialogClickListener mOnDialogClickListener;

    private QuickBottomImagePreviewBinding mBinding;

    /**
     * 按钮监听接口
     */
    public interface OnDialogClickListener {

        /**
         * 相册
         */
        void save();

        /**
         * 取消
         */
        void exit();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        mBinding = QuickBottomImagePreviewBinding.inflate(getLayoutInflater());

        mBinding.save.setOnClickListener(this);
        mBinding.exit.setOnClickListener(this);

        dialog.setContentView(mBinding.getRoot());

        dialog.getWindow().findViewById(R.id.design_bottom_sheet)
                // #00ffffff
                .setBackgroundResource(R.color.transparent);

        return dialog;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.save) {
            mOnDialogClickListener.save();
            dismiss();
        } else if (i == R.id.exit) {
            mOnDialogClickListener.exit();
            dismiss();
        }
    }

    public static class Builder {

        private BottomDialogImagePreviewFragment mDialogFragment;

        public Builder() {
            mDialogFragment = new BottomDialogImagePreviewFragment();
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
