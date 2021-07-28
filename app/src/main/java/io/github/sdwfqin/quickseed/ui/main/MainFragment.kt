package io.github.sdwfqin.quickseed.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.blankj.utilcode.util.ToastUtils
import io.github.sdwfqin.quicklib.base.BaseFragment
import io.github.sdwfqin.quicklib.dialog.QuickSimpleHintDialog
import io.github.sdwfqin.quicklib.imagepreview.ImagePreviewActivity
import io.github.sdwfqin.quickseed.R
import io.github.sdwfqin.quickseed.constants.ArouterConstants
import io.github.sdwfqin.quickseed.databinding.FragmentMainBinding
import io.github.sdwfqin.quickseed.ui.example.sortlist.SortListActivity
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
        "上传图片九宫格",
        "自定义验证码/密码View",
        "跑马灯Demo",
        "Camerax（支持二维码识别）",
        "VLayoutDemo",
        "展示原生SVG图片",
        "WebView",
        "悬浮窗与截图",
        "支持换肤的弹窗",
        "Mvvm Demo",
        "圆（方）形加载进度条",
        "仿京东分类列表",
    )

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentMainBinding {
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
                    strings.add("https://sdwfqin1-1252249614.cos.ap-beijing-1.myqcloud.com/blog/service_v1.0.png");
                    strings.add("https://sdwfqin1-1252249614.costj.myqcloud.com/blog/shopping.gif");
                    ImagePreviewActivity.launch(strings);
                }
                1 -> {
                    ARouter.getInstance().build(ArouterConstants.COMPONENTS_PICTUREUPLOAD)
                        .navigation();
                }
                2 -> {
                    ARouter.getInstance().build(ArouterConstants.COMPONENTS_PAYPWD).navigation();
                }
                3 -> {
                    ARouter.getInstance().build(ArouterConstants.COMPONENTS_AUTOPOLLRECYCLER)
                        .navigation();
                }
                4 -> {
                    ARouter.getInstance().build(ArouterConstants.COMPONENTS_CAMERAX).navigation();
                }
                5 -> {
                    ARouter.getInstance().build(ArouterConstants.COMPONENTS_VLAYOUTSAMPLE)
                        .navigation();
                }
                6 -> {
                    ARouter.getInstance().build(ArouterConstants.COMPONENTS_SHOWSVG).navigation();
                }
                7 -> {
                    ARouter.getInstance().build(ArouterConstants.COMPONENTS_WEBVIEW).navigation();
                }
                8 -> {
                    ARouter.getInstance()
                        .build(ArouterConstants.COMPONENTS_WINDOWFLOATANDSCREENSHOT).navigation();
                }
                9 -> {
                    showCustomDialog()
                }
                10 -> {
                    ARouter.getInstance().build(ArouterConstants.COMPONENTS_MVVM).navigation();
                }
                11 -> {
                    ARouter.getInstance().build(ArouterConstants.COMPONENTS_CIRCLEPROGRESSDEMO)
                        .navigation();
                }
                12 -> {
                    startActivity(Intent(mContext, SortListActivity::class.java))
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
            .setOnClickListener(object :
                QuickSimpleHintDialog.OnDialogClickListener {
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