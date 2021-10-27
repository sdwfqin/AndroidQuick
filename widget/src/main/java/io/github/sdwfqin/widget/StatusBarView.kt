package io.github.sdwfqin.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.BarUtils;

public class StatusBarView extends View {

    public StatusBarView(Context context) {
        this(context, null);
    }

    public StatusBarView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StatusBarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setBackgroundColor(getContext().getResources().getColor(R.color.colorPrimary));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthSpec = MeasureSpec.getSize(widthMeasureSpec);

        //高度我们要设置成statusbar的高度
        int measureHeight = BarUtils.getStatusBarHeight();
        setMeasuredDimension(widthSpec, measureHeight);

    }
}
