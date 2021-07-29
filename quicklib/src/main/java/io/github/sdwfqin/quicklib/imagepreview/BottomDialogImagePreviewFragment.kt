package io.github.sdwfqin.quicklib.imagepreview

import android.app.Dialog
import android.os.Bundle
import android.view.View
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import io.github.sdwfqin.quicklib.R
import io.github.sdwfqin.quicklib.databinding.QuickBottomImagePreviewBinding

/**
 * 描述：保存图片弹窗
 *
 * @author 张钦
 * @date 2018/1/16
 */
class BottomDialogImagePreviewFragment : BottomSheetDialogFragment(), View.OnClickListener {

    private var mOnDialogClickListener: OnDialogClickListener? = null
    private lateinit var mBinding: QuickBottomImagePreviewBinding

    /**
     * 按钮监听接口
     */
    interface OnDialogClickListener {
        /**
         * 相册
         */
        fun save()

        /**
         * 取消
         */
        fun exit()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        mBinding = QuickBottomImagePreviewBinding.inflate(
            layoutInflater
        )
        mBinding.save.setOnClickListener(this)
        mBinding.exit.setOnClickListener(this)
        dialog.setContentView(mBinding.root)
        dialog.window?.findViewById<View>(R.id.design_bottom_sheet)
            ?.setBackgroundResource(R.color.transparent)
        return dialog
    }

    override fun onClick(v: View) {
        val i = v.id
        if (i == R.id.save) {
            mOnDialogClickListener?.save()
            dismiss()
        } else if (i == R.id.exit) {
            mOnDialogClickListener?.exit()
            dismiss()
        }
    }

    class Builder {
        private val mDialogFragment: BottomDialogImagePreviewFragment =
            BottomDialogImagePreviewFragment()

        fun setOnClickListener(onClickListener: OnDialogClickListener): Builder {
            mDialogFragment.mOnDialogClickListener = onClickListener
            return this
        }

        fun builder(): BottomSheetDialogFragment {
            return mDialogFragment
        }

    }
}