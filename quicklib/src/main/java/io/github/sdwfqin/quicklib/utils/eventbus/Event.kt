package io.github.sdwfqin.quicklib.utils.eventbus

/**
 * 描述：EventBus事件对象
 *
 * @author 张钦
 * @date 2017/12/14
 */
class Event<T> {
    var code: Int
    var data: T? = null
        private set

    constructor(code: Int) {
        this.code = code
    }

    constructor(code: Int, data: T) {
        this.code = code
        this.data = data
    }

    fun setData(data: T) {
        this.data = data
    }
}