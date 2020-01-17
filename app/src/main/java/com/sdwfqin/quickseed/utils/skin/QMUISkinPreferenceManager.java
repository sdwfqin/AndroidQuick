package com.sdwfqin.quickseed.utils.skin;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by cgspine on 2018/1/14.
 */
public class QMUISkinPreferenceManager {

    private static SharedPreferences sPreferences;
    private static QMUISkinPreferenceManager sSkinPreferenceManager = null;

    private static final String APP_SKIN_INDEX = "app_skin_index";

    private QMUISkinPreferenceManager(Context context) {
        sPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
    }

    public static final QMUISkinPreferenceManager getInstance(Context context) {
        if (sSkinPreferenceManager == null) {
            sSkinPreferenceManager = new QMUISkinPreferenceManager(context);
        }
        return sSkinPreferenceManager;
    }

    public void setSkinIndex(int index) {
        SharedPreferences.Editor editor = sPreferences.edit();
        editor.putInt(APP_SKIN_INDEX, index);
        editor.apply();
    }

    public int getSkinIndex() {
        return sPreferences.getInt(APP_SKIN_INDEX, QMUISkinCustManager.SKIN_BLUE);
    }
}
