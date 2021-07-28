package io.github.sdwfqin.quicklib.utils.eventbus

import org.greenrobot.eventbus.EventBus

/**
 * 描述：EventBus工具类
 *
 * @author 张钦
 * @date 2017/12/14
 */
object EventBusUtils {

    /**
     * 注册
     *
     * @param subscriber
     */
    fun register(subscriber: Any?) {
        EventBus.getDefault().register(subscriber)
    }

    /**
     * 反注册
     *
     * @param subscriber
     */
    fun unregister(subscriber: Any?) {
        EventBus.getDefault().unregister(subscriber)
    }

    /**
     * 发送普通事件
     *
     * @param event
     */
    fun sendEvent(event: Event<*>?) {
        EventBus.getDefault().post(event)
    }

    /**
     * 发送粘性事件
     *
     * @param event
     */
    fun sendStickyEvent(event: Event<*>?) {
        EventBus.getDefault().postSticky(event)
    }

    /**
     * 移除粘性事件
     *
     * @param event
     */
    fun removeStickyEvent(event: Event<*>?) {
        EventBus.getDefault().removeStickyEvent(event)
    }
}