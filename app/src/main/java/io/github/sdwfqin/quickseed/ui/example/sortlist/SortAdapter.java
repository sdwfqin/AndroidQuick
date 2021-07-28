package io.github.sdwfqin.quickseed.ui.example.sortlist;

import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import io.github.sdwfqin.quickseed.R;

public class SortAdapter extends BaseQuickAdapter<SortBean, BaseViewHolder> {

    private int checkedPosition;
    private OnCallbackListener mOnCallbackListener;

    public SortAdapter(@Nullable List<SortBean> data) {
        super(R.layout.item_sort_title, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, SortBean sortBean) {
        TextView title = baseViewHolder.getView(R.id.title);
        title.setText(sortBean.getTitle());
        if (checkedPosition == baseViewHolder.getAdapterPosition()) {
            title.setBackgroundColor(getContext().getResources().getColor(R.color.frame_gray_background_color));
            title.setTextColor(getContext().getResources().getColor(R.color.colorPrimary));
        } else {
            title.setBackgroundColor(getContext().getResources().getColor(R.color.frame_white_background_color));
            title.setTextColor(getContext().getResources().getColor(R.color.text_black_color));
        }
    }

    public void setCheckedPosition(int checkedPosition) {
        this.checkedPosition = checkedPosition;
        notifyDataSetChanged();
        if (mOnCallbackListener != null){
            mOnCallbackListener.OnCheckedChange(checkedPosition);
        }
    }

    public int getCheckedPosition() {
        return checkedPosition;
    }

    public void setOnCallbackListener(OnCallbackListener onCallbackListener) {
        mOnCallbackListener = onCallbackListener;
    }

    public interface OnCallbackListener {
        void OnCheckedChange(int position);
    }
}
