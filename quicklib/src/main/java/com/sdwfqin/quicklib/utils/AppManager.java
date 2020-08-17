package com.sdwfqin.quicklib.utils;

import android.app.Activity;

import java.util.Stack;

/**
 * 描述：Activity栈管理
 * <p>
 * 现在有了更完善的工具 {@link com.blankj.utilcode.util.ActivityUtils}
 *
 * @author 张钦
 * @date 2017/11/11
 */
@Deprecated
public class AppManager {

    private static Stack<Activity> activityStack = new Stack<>();

    /**
     * 获取Activity栈数量
     */
    public static int getActivitySize() {
        return activityStack.size();
    }

    /**
     * 添加Activity到堆栈
     */
    public static void addActivity(Activity activity) {
        activityStack.push(activity);
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public static Activity currentActivity() {
        return activityStack.lastElement();
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public static void finishCurrentActivity() {
        Activity activity = activityStack.pop();
        activity.finish();
    }

    /**
     * 结束指定的Activity
     */
    public static void finishActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
    }

    /**
     * 移除指定的Activity
     */
    public static void removeActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public static void finishActivity(Class<?> cls) {
        finishActivity(getActivity(cls));
    }

    /**
     * 获取指定类名的Activity
     */
    public static Activity getActivity(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                return activity;
            }
        }
        return null;
    }

    /**
     * 结束所有Activity
     */
    public static void finishAllActivity() {
        for (Activity activity : activityStack) {
            if (activity != null) {
                activity.finish();
            }
        }
        activityStack.clear();
    }

    /**
     * 结束除指定Activity之外的所有Activity
     */
    public static void finishAllActivity(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity != null) {
                if (!activity.getClass().equals(cls)) {
                    activity.finish();
                }
            }
        }
    }

}
