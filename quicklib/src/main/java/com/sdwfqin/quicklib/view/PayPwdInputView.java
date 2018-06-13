package com.sdwfqin.quicklib.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v7.widget.AppCompatEditText;
import android.text.InputFilter;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.blankj.utilcode.util.ConvertUtils;
import com.sdwfqin.quicklib.R;
import com.sdwfqin.quicklib.utils.QuickUtils;

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
    private final static int Type_weChat = 0;
    private final static int Type_bottomLine = 1;
    private final static int Type_block = 2;

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
     * 分割线开始的坐标x
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
     * TODO: 自适用宽高（微信样式）
     */
    private boolean autoSize = false;

    /**
     * view的高度
     */
    private int height;
    private int width;
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
        this.setBackgroundColor(Color.TRANSPARENT);
        // 设置不显示光标
        this.setCursorVisible(false);
        // 设置EditText的最大长度
        this.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxCount)});

        this.getViewTreeObserver()
                .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        ViewGroup.LayoutParams layoutParams = getLayoutParams();
                        layoutParams.height = height;
                        layoutParams.width = width;
                        setLayoutParams(layoutParams);
                    }
                });
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
        maxCount = typedArray.getInt(R.styleable.PayPwdInputView_maxCount, maxCount);
        circleColor = typedArray.getColor(R.styleable.PayPwdInputView_circleColor, circleColor);
        textSize = typedArray.getDimension(R.styleable.PayPwdInputView_textSize, textSize);
        bottomLineColor = typedArray.getColor(R.styleable.PayPwdInputView_bottomLineColor, bottomLineColor);
        pwdRadius = typedArray.getDimension(R.styleable.PayPwdInputView_pwdRadius, pwdRadius);

        divideLineWidth = typedArray.getDimension(R.styleable.PayPwdInputView_divideLineWidth, divideLineWidth);
        divideLineColor = typedArray.getColor(R.styleable.PayPwdInputView_divideLineColor, divideLineColor);
        psdType = typedArray.getInt(R.styleable.PayPwdInputView_psdType, psdType);
        rectAngle = typedArray.getDimension(R.styleable.PayPwdInputView_rectAngle, rectAngle);
        isPwd = typedArray.getBoolean(R.styleable.PayPwdInputView_isPwd, isPwd);
        autoSize = typedArray.getBoolean(R.styleable.PayPwdInputView_autoSize, autoSize);

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
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        height = h;
        width = w;

        if (psdType == Type_weChat && autoSize) {
            int w1 = height * maxCount;
            if (w1 < width) {
                width = w1;
            } else {
                height = width / maxCount;
            }
        }
        h = height;
        w = width;

        super.onSizeChanged(w, h, oldw, oldh);

        divideLineWStartX = w / maxCount;

        startX = w / maxCount / 2;
        startY = h / 2;

        bottomLineLength = w / (maxCount + 2);

        rectF.set(0, 0, width, height);

    }

    @Override
    protected void onDraw(Canvas canvas) {

        if (psdType == Type_weChat) {
            drawWeChatBorder(canvas);
        } else if (psdType == Type_bottomLine) {
            drawBottomBorder(canvas);
        } else if (psdType == Type_block) {
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

        // startX是底部横线的长度 cx是x轴横线的中间位置
        // 画底部横线
        for (int i = 0; i < maxCount; i++) {
            cX = startX + i * 2 * startX;
            //上面的横线
            canvas.drawLine(cX - bottomLineLength / 2,
                    0,
                    cX + bottomLineLength / 2,
                    0, bottomLinePaint);
            //左面的竖线
            canvas.drawLine(cX - bottomLineLength / 2,
                    0,
                    cX - bottomLineLength / 2,
                    height, bottomLinePaint);
            //右左面的竖线
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
        List<String> list = QuickUtils.subStringToList(getPasswordString());
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
