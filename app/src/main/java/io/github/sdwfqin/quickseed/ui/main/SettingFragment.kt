package io.github.sdwfqin.quickseed.ui.main

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.ToastUtils
import com.qmuiteam.qmui.widget.dialog.QMUIDialog.MenuDialogBuilder
import io.github.sdwfqin.quicklib.base.BaseFragment
import io.github.sdwfqin.quickseed.constants.ArouterConstants
import io.github.sdwfqin.quickseed.databinding.FragmentSettingBinding
import io.github.sdwfqin.samplecommonlibrary.utils.SkinManagerUtils

/**
 * 配置页面
 *
 *
 *
 * @author 张钦
 * @date 2020-01-15
 */
@Route(path = ArouterConstants.MAIN_MINE)
class SettingFragment : BaseFragment<FragmentSettingBinding>() {

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentSettingBinding {
        return FragmentSettingBinding.inflate(inflater, container, false)
    }

    override fun initEventAndData() {
        mBinding.topBar.setTitle("配置")
    }

    override fun initClickListener() {
        mBinding.llChangeSkin.setOnClickListener { changeSkin() }
    }

    private fun changeSkin() {
        val items = arrayOf("跟随系统", "浅色", "深色")
        MenuDialogBuilder(activity)
            .addItems(items) { dialog: DialogInterface, which: Int ->
                SkinManagerUtils.changeSkin(which)
                ToastUtils.showShort("你选择了 " + items[which])
                dialog.dismiss()
            }
            .create().show()
    }
}