package com.sdwfqin.quickseed.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.sdwfqin.quicklib.base.BaseFragment;
import com.sdwfqin.quickseed.base.ArouterConstants;
import com.sdwfqin.quickseed.databinding.FragmentSettingBinding;
import com.sdwfqin.quickseed.utils.skin.QMUISkinCustManager;

/**
 * 配置页面
 * <p>
 *
 * @author 张钦
 * @date 2020-01-15
 */
@Route(path = ArouterConstants.MAIN_MINE)
public class SettingFragment extends BaseFragment<FragmentSettingBinding> {

    @Override
    protected FragmentSettingBinding getViewBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return FragmentSettingBinding.inflate(inflater, container, false);
    }

    @Override
    protected void initEventAndData() {
        mBinding.topBar.setTitle("配置");
    }

    @Override
    protected void initClickListener() {
        mBinding.llChangeSkin.setOnClickListener(v -> changeSkin());
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
