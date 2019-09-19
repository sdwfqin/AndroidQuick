package com.sdwfqin.widget;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 描述：颤抖的按钮
 *
 * @author 张钦
 * @date 2018/1/30
 */
public class TrembleButton extends AppCompatTextView {

    private int durationMax = 6000;
    private int durationMin = 5000;
    private int translationMax = 20;
    private int translationMin = 15;

    public TrembleButton(Context context) {
        this(context, null);
    }

    public TrembleButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TrembleButton);
            durationMax = typedArray.getInteger(R.styleable.TrembleButton_quick_durationMax, 6000);
            durationMin = typedArray.getInteger(R.styleable.TrembleButton_quick_durationMin, 5000);
            translationMax = typedArray.getInteger(R.styleable.TrembleButton_quick_translationMax, 20);
            translationMin = typedArray.getInteger(R.styleable.TrembleButton_quick_translationMin, 15);
            typedArray.recycle();
        }

        setAnimFloat(this);
    }

    /**
     * 给指定的View设置浮动效果
     *
     * @param view
     * @return
     */
    private AnimatorSet setAnimFloat(View view) {
        List<Animator> animators = new ArrayList<>();
        //getRandomDp()得到一个随机的值
        ObjectAnimator translationXAnim = ObjectAnimator.ofFloat(view, "translationX", 0f, getRandom(), getRandom(), 0);
        translationXAnim.setDuration(getRandomTime());
        translationXAnim.setRepeatCount(ValueAnimator.INFINITE);//无限循环
        // translationXAnim.setRepeatMode(ValueAnimator.INFINITE);//
        translationXAnim.setInterpolator(new LinearInterpolator());
        translationXAnim.start();
        animators.add(translationXAnim);

        ObjectAnimator translationYAnim = ObjectAnimator.ofFloat(view, "translationY", 0f, getRandom(), getRandom(), 0);
        translationYAnim.setDuration(getRandomTime());
        translationYAnim.setRepeatCount(ValueAnimator.INFINITE);
        // translationYAnim.setRepeatMode(ValueAnimator.INFINITE);
        translationXAnim.setInterpolator(new LinearInterpolator());
        translationYAnim.start();
        animators.add(translationYAnim);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(animators);
        animatorSet.start();
        return animatorSet;
    }

    /**
     * 产生（5000 - 6000）随机数
     *
     * @return
     */
    private int getRandomTime() {

        int min = durationMax - durationMin;
        int max = durationMax - min;

        Random random = new Random();
        return (random.nextInt(min) + max);
    }

    /**
     * 产生（15 - 20）随机数
     *
     * @return
     */
    private int getRandom() {

        int min = translationMax - translationMin;
        int max = translationMax - min;

        Random random = new Random();
        boolean b = random.nextBoolean();
        if (b) {
            return random.nextInt(min) + max;
        }
        return (random.nextInt(min) + max) * -1;
    }
}
