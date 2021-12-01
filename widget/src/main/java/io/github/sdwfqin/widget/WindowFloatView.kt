package io.github.sdwfqin.widget

import android.app.Application
import android.content.Context
import android.graphics.PixelFormat
import android.os.Build
import android.view.*
import android.view.View.OnTouchListener
import androidx.annotation.IdRes

/**
 * 悬浮窗View
 *
 * @author 张钦
 * @date 2020/4/10
 */
abstract class WindowFloatView(private val context: Application) {
    private val windowManager: WindowManager =
        context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    private val params: WindowManager.LayoutParams = WindowManager.LayoutParams()

    private lateinit var rootView: View

    //获取当前悬浮窗是否展示
    private var isShowing = false
    private var mCreate = false
    private var isCanMove = false

    init {
        //窗口管理器
        //布局参数
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            params.type = WindowManager.LayoutParams.TYPE_PHONE
        }
        params.format = PixelFormat.RGBA_8888
        params.flags = getWindowFlags()
        params.gravity = getWindowGravity()
        params.width = getWidth()
        params.height = getHeight()
        if (getCreateAnimator() != 0) {
            params.windowAnimations = getCreateAnimator()
        }
    }

    fun show() {
        if (isShowing) {
            rootView.visibility = View.VISIBLE
            return
        }
        if (!mCreate) {
            dispathOnCreate()
        }
        windowManager.addView(rootView, params)
        isShowing = true
        onStart()
    }

    private fun dispathOnCreate() {
        if (!mCreate) {
            create()
            mCreate = true
        }
    }

    private fun create() {
        rootView = LayoutInflater.from(context).inflate(getLayoutView(), null)
        onCreate(rootView, params)
        if (isCanMove) {
            rootView.setOnTouchListener(FloatOnTouchListener())
        }
    }

    //留给子类修改布局参数使用。
    protected open fun onCreate(rootView: View, layoutParams: WindowManager.LayoutParams) {}

    fun dismiss() {
        if (!isShowing) {
            return
        }
        try {
            onStop()
            windowManager.removeViewImmediate(rootView)
        } finally {
            isShowing = false
            mCreate = false
            //这里可以还原参数
        }
    }

    fun hide() {
        rootView.visibility = View.GONE
    }

    protected fun <T : View> findViewById(@IdRes id: Int): T {
        return rootView.findViewById(id)
    }

    fun setCanMove(isCan: Boolean) {
        isCanMove = isCan
    }

    //获取当前悬浮窗是否展示
    fun isShowing(): Boolean {
        return isShowing
    }

    fun getContext(): Application {
        return context
    }

    fun getWindowManager(): WindowManager {
        return windowManager
    }

    /**
     * 悬浮窗布局
     */
    protected abstract fun getLayoutView(): Int

    /**
     * 界面加载完成
     */
    protected open fun onStart() {}

    /**
     * 界面销毁
     */
    protected open fun onStop() {}

    /**
     * 高度
     */
    protected abstract fun getHeight(): Int

    /**
     * 宽度
     */
    protected abstract fun getWidth(): Int

    protected open fun getWindowFlags(): Int {
        return WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
    }

    protected open fun getWindowGravity(): Int {
        return Gravity.LEFT or Gravity.TOP
    }

    protected open fun getCreateAnimator(): Int {
        return -1
    }

    private inner class FloatOnTouchListener : OnTouchListener {

        private var x = 0
        private var y = 0

        override fun onTouch(v: View, event: MotionEvent): Boolean {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    x = event.rawX.toInt()
                    y = event.rawY.toInt()
                }
                MotionEvent.ACTION_MOVE -> {
                    val nowX = event.rawX.toInt()
                    val nowY = event.rawY.toInt()
                    val movedX = nowX - x
                    val movedY = nowY - y
                    x = nowX
                    y = nowY
                    params.x = params.x + movedX
                    params.y = params.y + movedY

                    // 更新悬浮窗控件布局
                    windowManager.updateViewLayout(v, params)
                }
                else -> {}
            }
            return false
        }
    }
}