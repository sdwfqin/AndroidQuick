package com.sdwfqin.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.InputFilter;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatEditText;

import com.blankj.utilcode.util.ConvertUtils;
import com.sdwfqin.widget.utils.WidgetUtils;

import java.util.List;

/**
 * 描述：验证码/密码
 *
 * @author zhangqin
 */
public class PayPwdInputView extends AppCompatEditText {

    /**
     * 输入框类型
     */
    private final static int TYPE_WECHAT = 0;
    private final static int TYPE_BOTTOM_LINE = 1;
    private final static int TYPE_BLOCK = 2;

    private final static int DEFAULT_HEIGHT = ConvertUtils.dp2px(45);

    private Context mContext;

    /**
     * 实心圆的半径
     */
    private float pwdRadius = ConvertUtils.dp2px(6);

    /**
     * 当前输入密码位数
     */
    private int textLength = 0;

    /**
     * 最大输入位数
     */
    private int maxCount = 6;

    /**
     * 圆或文字的颜色   默认BLACK
     */
    private int circleColor = Color.BLACK;

    /**
     * 圆或文字的颜色   默认BLACK
     */
    private float textSize = ConvertUtils.sp2px(18);

    /**
     * 底部线的颜色   默认GRAY
     */
    private int bottomLineColor = Color.GRAY;

    /**
     * 分割线的颜色
     */
    private int borderColor = Color.GRAY;

    /**
     * 分割线开始的坐标x（WECHAT样式用）
     */
    private int divideLineWStartX;

    /**
     * 分割线的宽度  默认1dp
     */
    private float divideLineWidth = ConvertUtils.dp2px(1);

    /**
     * 竖直分割线的颜色
     */
    private int divideLineColor = Color.GRAY;

    /**
     * 输入框类型(weChat or bottom）
     */
    private int psdType = 0;

    /**
     * 是否是明文(0不可见，1可见)
     */
    private boolean isPwd = true;

    /**
     * 自适用宽高（微信样式）
     */
    private boolean autoSize = false;

    /**
     * view的高度
     */
    private int height;
    private int width;
    /**
     * x轴线长度与空白
     */
    private int bottomLineLength;

    /**
     * 第一个圆开始绘制的圆心坐标
     */
    private float startX;
    private float startY;

    /**
     * 圆心x坐标
     */
    private float cX;

    /**
     * 表示一块矩形区域(left <= right and top <= bottom)
     */
    private RectF rectF = new RectF();

    /**
     * 分割线的画笔
     */
    private Paint borderPaint;

    /**
     * 矩形边框的圆角
     */
    private float rectAngle = 0;

    /**
     * 竖直分割线的画笔
     */
    private Paint divideLinePaint;

    /**
     * 圆的画笔
     */
    private Paint circlePaint;

    /**
     * 文字的画笔
     */
    private Paint textPaint;

    /**
     * 底部线的画笔
     */
    private Paint bottomLinePaint;

    /**
     * 文字基线（明文时处置居中）
     */
    private int mBaseLineY;

    /**
     * 输入完成监听
     */
    private OnPasswordListener mListener;

    /**
     * 构造方法
     *
     * @param context
     * @param attrs
     */
    public PayPwdInputView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;

        // 获取attr
        getAtt(attrs);
        // 初始化画笔
        initPaint();

        // 设置EditText背景为透明
        setBackgroundColor(Color.TRANSPARENT);
        // 设置不显示光标
        setCursorVisible(false);
        // 设置EditText的最大长度
        setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxCount)});
    }

    /**
     * 读取自定义属性值
     * <p>
     * getDimension：返回类型为float，
     * getDimensionPixelSize：返回类型为int，由浮点型转成整型时，采用四舍五入原则。
     * getDimensionPixelOffset：返回类型为int，由浮点型转成整型时，原则是忽略小数点部分
     *
     * @param attrs
     */
    private void getAtt(AttributeSet attrs) {
        TypedArray typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.PayPwdInputView);
        maxCount = typedArray.getInt(R.styleable.PayPwdInputView_quick_maxCount, maxCount);
        circleColor = typedArray.getColor(R.styleable.PayPwdInputView_quick_circleColor, circleColor);
        textSize = typedArray.getDimension(R.styleable.PayPwdInputView_quick_textSize, textSize);
        bottomLineColor = typedArray.getColor(R.styleable.PayPwdInputView_quick_bottomLineColor, bottomLineColor);
        pwdRadius = typedArray.getDimension(R.styleable.PayPwdInputView_quick_pwdRadius, pwdRadius);

        divideLineWidth = typedArray.getDimension(R.styleable.PayPwdInputView_quick_divideLineWidth, divideLineWidth);
        divideLineColor = typedArray.getColor(R.styleable.PayPwdInputView_quick_divideLineColor, divideLineColor);
        psdType = typedArray.getInt(R.styleable.PayPwdInputView_quick_psdType, psdType);
        rectAngle = typedArray.getDimension(R.styleable.PayPwdInputView_quick_rectAngle, rectAngle);
        isPwd = typedArray.getBoolean(R.styleable.PayPwdInputView_quick_isPwd, isPwd);
        autoSize = typedArray.getBoolean(R.styleable.PayPwdInputView_quick_autoSize, autoSize);

        typedArray.recycle();
    }

    /**
     * 初始化画笔
     */
    private void initPaint() {

        circlePaint = getPaint(divideLineWidth, Paint.Style.FILL, circleColor);

        textPaint = getPaint(divideLineWidth, Paint.Style.FILL, circleColor);
        textPaint.setTextSize(textSize);
        // 水平居中
        textPaint.setTextAlign(Paint.Align.CENTER);

        bottomLinePaint = getPaint(divideLineWidth, Paint.Style.FILL, bottomLineColor);

        borderPaint = getPaint(divideLineWidth, Paint.Style.STROKE, borderColor);

        divideLinePaint = getPaint(divideLineWidth, Paint.Style.FILL, borderColor);

    }

    /**
     * 设置画笔
     *
     * @param strokeWidth 画笔宽度
     * @param style       画笔风格
     * @param color       画笔颜色
     * @return
     */
    private Paint getPaint(float strokeWidth, Paint.Style style, int color) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStrokeWidth(strokeWidth);
        paint.setStyle(style);
        paint.setColor(color);
        paint.setAntiAlias(true);

        return paint;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = measureWidth(widthMeasureSpec);
        height = measureHeight(heightMeasureSpec);

        if (psdType == TYPE_WECHAT && autoSize) {
            int w1 = height * maxCount;
            if (w1 < width) {
                width = w1;
            } else {
                height = width / maxCount;
            }
        }

        setMeasuredDimension(width, height);
    }

//    @Override
//    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
//        super.onSizeChanged(w, h, oldw, oldh);
//
//        // 不设置padding有毛病
//        setPadding(0, 0, 0, 0);
//
//        // 微信样式用
//        divideLineWStartX = w / maxCount;
//
//        // 圆心坐标
//        startX = w / maxCount / 2;
//        startY = h / 2;
//
//        // 默认情况下x轴边线长度
//        bottomLineLength = w / (maxCount + 2);
//
//        // 微信样式画一个大长方形
//        rectF.set(0, 0, width, height);
//
//    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        int w = right - left;
        int h = bottom - top;

        // 不设置padding有毛病
        setPadding(0, 0, 0, 0);

        // 微信样式用
        divideLineWStartX = w / maxCount;

        // 圆心坐标
        startX = w / maxCount / 2;
        startY = h / 2;

        // 默认情况下x轴边线长度
        bottomLineLength = w / (maxCount + 2);

        // 微信样式画一个大长方形
        rectF.set(0, 0, width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        if (psdType == TYPE_WECHAT) {
            drawWeChatBorder(canvas);
        } else if (psdType == TYPE_BOTTOM_LINE) {
            drawBottomBorder(canvas);
        } else if (psdType == TYPE_BLOCK) {
            drawBlockBorder(canvas);
        }

        if (isPwd) {
            drawPsdCircle(canvas);
        } else {
            drawText(canvas);
        }
    }

    /**
     * 画微信支付密码的样式
     *
     * @param canvas
     */
    private void drawWeChatBorder(Canvas canvas) {

        // 画一个长方形框
        canvas.drawRoundRect(rectF, rectAngle, rectAngle, borderPaint);

        // 竖线，两侧已经有了
        for (int i = 0; i < maxCount - 1; i++) {
            // 画竖线
            canvas.drawLine((i + 1) * divideLineWStartX,
                    0,
                    (i + 1) * divideLineWStartX,
                    height,
                    divideLinePaint);
        }

    }

    /**
     * 画底部显示的分割线
     *
     * @param canvas
     */
    private void drawBottomBorder(Canvas canvas) {

        // startX是底部横线的长度 cx是x轴横线的中间位置
        // 画底部横线
        for (int i = 0; i < maxCount; i++) {
            cX = startX + i * 2 * startX;
            canvas.drawLine(cX - bottomLineLength / 2,
                    height,
                    cX + bottomLineLength / 2,
                    height, bottomLinePaint);
        }
    }

    /**
     * 画小方块
     *
     * @param canvas
     */
    private void drawBlockBorder(Canvas canvas) {

        // x轴边线y轴边线相同
        bottomLineLength = height;
        // startX是底部横线的长度 cx是x轴横线的中间位置
        // 画底部横线
        for (int i = 0; i < maxCount; i++) {
            cX = startX + i * 2 * startX;
            // 上面的横线
            canvas.drawLine(cX - bottomLineLength / 2,
                    0,
                    cX + bottomLineLength / 2,
                    0, bottomLinePaint);
            // 左面的竖线
            canvas.drawLine(cX - bottomLineLength / 2,
                    0,
                    cX - bottomLineLength / 2,
                    height, bottomLinePaint);
            // 右面的竖线
            canvas.drawLine(cX + bottomLineLength / 2,
                    0,
                    cX + bottomLineLength / 2,
                    height, bottomLinePaint);
            // 底下的横线
            canvas.drawLine(cX - bottomLineLength / 2,
                    height,
                    cX + bottomLineLength / 2,
                    height, bottomLinePaint);
        }
    }

    /**
     * 画文字，在onTextChanged中调用重绘方法
     *
     * @param canvas 画布
     */
    private void drawText(Canvas canvas) {

        // ========  文字垂直居中  ========
        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        // 为基线到字体上边框的距离,即上图中的top
        float top = fontMetrics.top;
        // 为基线到字体下边框的距离,即上图中的bottom
        float bottom = fontMetrics.bottom;
        // 基线中间点的y轴计算公式
        mBaseLineY = (int) (rectF.centerY() - top / 2 - bottom / 2);

        // 画文字
        List<String> list = WidgetUtils.subStringToList(getPasswordString());
        for (int i = 0; i < list.size(); i++) {
            canvas.drawText(list.get(i),
                    startX + i * 2 * startX,
                    mBaseLineY,
                    textPaint);
        }
    }

    /**
     * 画密码实心圆，在onTextChanged中调用重绘方法
     *
     * @param canvas 画布
     */
    private void drawPsdCircle(Canvas canvas) {
        // 画圆
        for (int i = 0; i < textLength; i++) {
            canvas.drawCircle(startX + i * 2 * startX,
                    startY,
                    pwdRadius,
                    circlePaint);
        }
    }

    /**
     * 监听文本变化
     *
     * @param text
     * @param start
     * @param lengthBefore
     * @param lengthAfter
     */
    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        textLength = text.toString().length();

        // 如果密码输入完成，调用回调方法
        if (textLength == maxCount && mListener != null) {
            mListener.onSuccess(getPasswordString());
        }
        // 重绘
        invalidate();
    }

    private int measureWidth(int measureSpec) {
        int result = 200;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        switch (specMode) {
            case MeasureSpec.UNSPECIFIED:
                result = specSize;
                break;
            case MeasureSpec.AT_MOST:
                result = Math.min(result, specSize);
                break;
            case MeasureSpec.EXACTLY:
                result = specSize;
                break;
            default:
        }
        return result;
    }

    private int measureHeight(int measureSpec) {
        int result = 200;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        switch (specMode) {
            // 父容器不对View有任何的限制，它不指定View的大小，即View想要多大就多大
            case MeasureSpec.UNSPECIFIED:
                result = specSize;
                break;
            // 最大值模式，即子View不能超过父控件的大小，对应LayoutParams中的wrap_content。
            case MeasureSpec.AT_MOST:
                // 如果高度为wrap_content，给一个默认值45dp
                // result = Math.min(result, specSize);
                result = DEFAULT_HEIGHT;
                break;
            // 精确测量的模式
            case MeasureSpec.EXACTLY:
                result = specSize;
                break;
            default:
        }
        return result;
    }

    /**
     * 保证光标始终在最后
     *
     * @param selStart
     * @param selEnd
     */
    @Override
    protected void onSelectionChanged(int selStart, int selEnd) {
        super.onSelectionChanged(selStart, selEnd);

        //保证光标始终在最后
        if (selStart == selEnd) {
            setSelection(getText().length());
        }
    }

    /**
     * 获取输入的密码
     *
     * @return String 用户输入的密码
     */
    public String getPasswordString() {
        return getText().toString().trim();
    }

    /**
     * 密码比较监听
     */
    public interface OnPasswordListener {

        /**
         * 输入完成
         *
         * @param text 输入的文字
         */
        void onSuccess(String text);
    }

    /**
     * 密码比较的外部接口
     *
     * @param listener 比较监听器
     */
    public void setOnPasswordListener(OnPasswordListener listener) {
        mListener = listener;
    }
}
