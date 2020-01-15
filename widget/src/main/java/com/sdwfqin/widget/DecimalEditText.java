package com.sdwfqin.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatEditText;

/**
 * 描述：Double类型的EditText
 *
 * @author 张钦
 * @date 2018/1/22
 */
public class DecimalEditText extends AppCompatEditText implements TextWatcher {

    private Context mContext;

    /**
     * 小数的位数
     */
    private int DECIMAL_DIGITS = 1;

    public DecimalEditText(Context context) {
        this(context, null);
    }

    public DecimalEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;

        this.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        this.setBackgroundResource(0);

        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.DecimalEditText);
            DECIMAL_DIGITS = typedArray.getInteger(R.styleable.DecimalEditText_quick_decimalLength, 2);
            typedArray.recycle();
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.toString().contains(".")) {
            if (s.length() - 1 - s.toString().indexOf(".") > DECIMAL_DIGITS) {
                s = s.toString().subSequence(0,
                        s.toString().indexOf(".") + DECIMAL_DIGITS + 1);
                this.setText(s);
                this.setSelection(s.length());
            }
        }
        if (s.toString().trim().substring(0).equals(".")) {
            s = "0" + s;
            this.setText(s);
            this.setSelection(2);
        }
        if (s.toString().startsWith("0")
                && s.toString().trim().length() > 1) {
            if (!s.toString().substring(1, 2).equals(".")) {
                this.setText(s.subSequence(0, 1));
                this.setSelection(1);
                return;
            }
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    public void setDecimalLength(int decimalLength) {
        DECIMAL_DIGITS = decimalLength;
    }
}
