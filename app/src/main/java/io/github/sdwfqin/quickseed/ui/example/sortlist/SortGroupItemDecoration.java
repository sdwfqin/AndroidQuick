package io.github.sdwfqin.quickseed.ui.example.sortlist;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ConvertUtils;

import io.github.sdwfqin.quickseed.R;

/**
 * https://blog.csdn.net/harvic880925/article/details/82959754
 * <p>
 *
 * @author 张钦
 * @date 2020/5/27
 */
public class SortGroupItemDecoration extends RecyclerView.ItemDecoration {

    private Paint mPaint;

    public SortGroupItemDecoration(Context context) {
        mPaint = new Paint();
        mPaint.setColor(context.getResources().getColor(R.color.frame_gray_background_color));
    }

    /**
     * 下方
     *
     * @param c
     * @param parent
     * @param state
     */
    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDraw(c, parent, state);
        int childCount = parent.getChildCount();

        for (int i = 0; i < childCount; i++) {
            c.drawPaint(mPaint);
        }
    }

    /**
     * 上方
     *
     * @param c
     * @param parent
     * @param state
     */
    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int childAdapterPosition = parent.getChildAdapterPosition(view);
        int itemViewType = parent.getAdapter().getItemViewType(childAdapterPosition);
        if (itemViewType == SortDetailAdapter.GROUP) {
            outRect.top = ConvertUtils.dp2px(10);
        }
    }
}
