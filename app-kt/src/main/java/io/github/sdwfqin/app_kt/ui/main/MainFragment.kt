package io.github.sdwfqin.app_kt.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.blankj.utilcode.util.ToastUtils
import com.sdwfqin.quicklib.base.BaseFragment
import com.sdwfqin.quicklib.dialog.QuickSimpleHintDialog
import com.sdwfqin.quicklib.imagepreview.ImagePreviewActivity
import io.github.sdwfqin.app_kt.R
import io.github.sdwfqin.app_kt.constants.ArouterConstants
import io.github.sdwfqin.app_kt.databinding.FragmentMainBinding
import java.util.*

/**
 * 描述：主Activity
 *
 * @author 张钦
 */
@Route(path = ArouterConstants.MAIN_HOME)
class MainFragment : BaseFragment<FragmentMainBinding>() {

    private val mTitle = arrayOf(
            "图片预览",
            "支持换肤的弹窗",
            "Camerax（支持二维码识别）",
            "MVVM DEMO"
    )

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): FragmentMainBinding {
        return FragmentMainBinding.inflate(inflater)
    }

    override fun initEventAndData() {
        mBinding.topBar.setTitle("组件")
        mBinding.list.adapter = ArrayAdapter(mContext, R.layout.item_list, R.id.tv_items, mTitle)
    }

    override fun initClickListener() {
        mBinding.list.onItemClickListener = AdapterView.OnItemClickListener { _, _, pos: Int, _ ->
            when (pos) {
                0 -> {
                    val strings: MutableList<String> = ArrayList()
                    strings.add("https://sdwfqin1-1252249614.cos.ap-beijing-1.myqcloud.com/blog/service_v1.0.png")
                    strings.add("https://sdwfqin1-1252249614.costj.myqcloud.com/blog/shopping.gif")
                    ImagePreviewActivity.launch(strings)
                }
                1 -> {
                    showCustomDialog()
                }
                2 -> {
                    ARouter.getInstance().build(ArouterConstants.COMPONENTS_CAMERAX).navigation()
                }
                3 -> {
                    ARouter.getInstance().build(ArouterConstants.COMPONENTS_MVVM).navigation()
                }
            }
        }
    }

    private fun showCustomDialog() {
        val hintDialog = QuickSimpleHintDialog.Builder()
                .setFollowSkin(true)
                .setTitleText("热更新测试33333")
                .setSubmitText("哈哈")
                .setCancelText("取消")
                .setShowCancelButton(true)
                .setOnClickListener(object : QuickSimpleHintDialog.OnDialogClickListener {
                    override fun submit(dialog: QuickSimpleHintDialog) {
                        ToastUtils.showShort("哈哈")
                        dialog.dismiss()
                    }

                    override fun cancel(dialog: QuickSimpleHintDialog) {
                        ToastUtils.showShort("取消")
                        dialog.dismiss()
                    }
                })
                .builder()
        hintDialog.show(childFragmentManager, "QuickSimpleHintDialog")
    }
}