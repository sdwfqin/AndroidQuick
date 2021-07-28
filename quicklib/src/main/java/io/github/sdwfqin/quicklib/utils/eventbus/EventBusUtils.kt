package io.github.sdwfqin.quicklib.utils.eventbus;

import org.greenrobot.eventbus.EventBus;

/**
 * 描述：EventBus工具类
 *
 * @author 张钦
 * @date 2017/12/14
 */
public class EventBusUtils {

    /**
     * 注册
     *
     * @param subscriber
     */
    public static void register(Object subscriber) {
        EventBus.getDefault().register(subscriber);
    }

    /**
     * 反注册
     *
     * @param subscriber
     */
    public static void unregister(Object subscriber) {
        EventBus.getDefault().unregister(subscriber);
    }

    /**
     * 发送普通事件
     *
     * @param event
     */
    public static void sendEvent(Event event) {
        EventBus.getDefault().post(event);
    }

    /**
     * 发送粘性事件
     *
     * @param event
     */
    public static void sendStickyEvent(Event event) {
        EventBus.getDefault().postSticky(event);
    }

    /**
     * 移除粘性事件
     *
     * @param event
     */
    public static void removeStickyEvent(Event event) {
        EventBus.getDefault().removeStickyEvent(event);
    }
}
