package com.sdwfqin.quickseed.utils.skin;

import android.content.Context;
import android.content.res.Configuration;

import com.qmuiteam.qmui.skin.QMUISkinManager;
import com.sdwfqin.quickseed.R;
import com.sdwfqin.quickseed.base.SampleApplication;

public class SkinManager {
    public static final int SKIN_BLUE = 1;
    public static final int SKIN_DARK = 2;


    public static void install(Context context) {
        QMUISkinManager skinManager = QMUISkinManager.defaultInstance(context);
        skinManager.addSkin(SKIN_BLUE, R.style.app_skin_blue);
        skinManager.addSkin(SKIN_DARK, R.style.app_skin_dark);
        boolean isDarkMode = (context.getResources().getConfiguration().uiMode
                & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES;
        int storeSkinIndex = SkinPreferenceManager.getInstance(context).getSkinIndex();
        if (isDarkMode && storeSkinIndex != SKIN_DARK) {
            skinManager.changeSkin(SKIN_DARK);
        } else if (!isDarkMode && storeSkinIndex == SKIN_DARK) {
            skinManager.changeSkin(SKIN_BLUE);
        }else{
            skinManager.changeSkin(storeSkinIndex);
        }
    }

    public static void changeSkin(int index) {
        QMUISkinManager.defaultInstance(SampleApplication.getContext()).changeSkin(index);
        SkinPreferenceManager.getInstance(SampleApplication.getContext()).setSkinIndex(index);
    }

    public static int getCurrentSkin() {
        return QMUISkinManager.defaultInstance(SampleApplication.getContext()).getCurrentSkin();
    }
}
