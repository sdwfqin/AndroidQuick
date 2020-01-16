package com.sdwfqin.quickseed.ui.main;

import android.content.DialogInterface;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatTextView;

import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.sdwfqin.quicklib.base.BaseFragment;
import com.sdwfqin.quickseed.R;
import com.sdwfqin.quickseed.utils.skin.QMUISkinCustManager;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 配置页面
 * <p>
 *
 * @author 张钦
 * @date 2020-01-15
 */
public class SettingFragment extends BaseFragment {

    @BindView(R.id.top_bar)
    QMUITopBar mTopBar;

    @Override
    protected int getLayout() {
        return R.layout.fragment_setting;
    }

    @Override
    protected void initEventAndData() {
        mTopBar.setTitle("配置");
    }

    @Override
    protected void lazyLoadShow() {

    }

    @OnClick(R.id.ll_change_skin)
    public void onViewClicked() {
        changeSkin();
    }

    private void changeSkin() {
        final String[] items = new String[]{"蓝色（默认）", "黑色"};
        new QMUIDialog.MenuDialogBuilder(getActivity())
                .addItems(items, (dialog, which) -> {
                    switch (which) {
                        case 0:
                            QMUISkinCustManager.changeSkin(QMUISkinCustManager.SKIN_BLUE);
                            break;
                        case 1:
                            QMUISkinCustManager.changeSkin(QMUISkinCustManager.SKIN_DARK);
                            break;
                    }
                    Toast.makeText(getActivity(), "你选择了 " + items[which], Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                })
                .create().show();
    }
}
