package com.sdwfqin.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.sdwfqin.widget.databinding.QuickAmountViewBinding;

/**
 * 描述：商品数量
 *
 * @author 张钦
 * @date 2018/1/18
 */
public class AmountView extends LinearLayout implements View.OnClickListener, TextWatcher {

    /**
     * 购买数量
     */
    private int amount = 1;
    /**
     * 商品库存
     */
    private int goods_storage = 1;

    private OnAmountChangeListener mListener;

    private QuickAmountViewBinding mBinding;

    public AmountView(Context context) {
        this(context, null);
    }

    public AmountView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mBinding = QuickAmountViewBinding.inflate(LayoutInflater.from(context));
        mBinding.btnDecrease.setOnClickListener(this);
        mBinding.btnIncrease.setOnClickListener(this);
        mBinding.etAmount.addTextChangedListener(this);

        TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(attrs, R.styleable.AmountView);
        int btnWidth = obtainStyledAttributes.getDimensionPixelSize(R.styleable.AmountView_quick_btnWidth, LayoutParams.WRAP_CONTENT);
        int tvWidth = obtainStyledAttributes.getDimensionPixelSize(R.styleable.AmountView_quick_tvWidth, 80);
        int tvTextSize = obtainStyledAttributes.getDimensionPixelSize(R.styleable.AmountView_quick_tvTextSize, 0);
        int btnTextSize = obtainStyledAttributes.getDimensionPixelSize(R.styleable.AmountView_quick_btnTextSize, 0);
        obtainStyledAttributes.recycle();

        LayoutParams btnParams = new LayoutParams(btnWidth, LayoutParams.MATCH_PARENT);
        mBinding.btnDecrease.setLayoutParams(btnParams);
        mBinding.btnIncrease.setLayoutParams(btnParams);
        if (btnTextSize != 0) {
            mBinding.btnDecrease.setTextSize(TypedValue.COMPLEX_UNIT_PX, btnTextSize);
            mBinding.btnIncrease.setTextSize(TypedValue.COMPLEX_UNIT_PX, btnTextSize);
        }

        LayoutParams textParams = new LayoutParams(tvWidth, LayoutParams.MATCH_PARENT);
        mBinding.etAmount.setLayoutParams(textParams);
        if (tvTextSize != 0) {
            mBinding.etAmount.setTextSize(tvTextSize);
        }
    }

    public void setOnAmountChangeListener(OnAmountChangeListener onAmountChangeListener) {
        mListener = onAmountChangeListener;
    }

    public void setGoods_storage(int goods_storage) {
        this.goods_storage = goods_storage;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btnDecrease) {
            if (amount > 1) {
                amount--;
                mBinding.etAmount.setText(amount + "");
            }
        } else if (i == R.id.btnIncrease) {
            if (amount < goods_storage) {
                amount++;
                mBinding.etAmount.setText(amount + "");
            }
        }

        mBinding.etAmount.clearFocus();

        if (mListener != null) {
            mListener.onAmountChange(this, amount);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.toString().isEmpty()) {
            return;
        }
        amount = Integer.valueOf(s.toString());
        if (amount > goods_storage) {
            mBinding.etAmount.setText(goods_storage + "");
            return;
        }

        if (mListener != null) {
            mListener.onAmountChange(this, amount);
        }
    }


    public interface OnAmountChangeListener {
        void onAmountChange(View view, int amount);
    }

}
