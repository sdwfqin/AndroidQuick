package io.github.sdwfqin.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.blankj.utilcode.util.BarUtils

class StatusBarView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthSpec = MeasureSpec.getSize(widthMeasureSpec)

        //高度我们要设置成statusbar的高度
        val measureHeight = BarUtils.getStatusBarHeight()
        setMeasuredDimension(widthSpec, measureHeight)
    }

    init {
        setBackgroundResource(R.color.color_primary_dark)
    }
}