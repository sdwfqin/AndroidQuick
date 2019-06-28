package com.sdwfqin.quickseed.adapter;

import com.alibaba.android.vlayout.LayoutHelper;
import com.chad.library.adapter.base.BaseViewHolder;
import com.sdwfqin.quickseed.R;
import com.sdwfqin.widget.recyclerview.BaseVlayoutAdapter;

import java.util.List;

/**
 * 历史行程
 * <p>
 *
 * @author 张钦
 * @date 2019-06-26
 */
public class StrokeHistoryAdapter extends BaseVlayoutAdapter<String> {

    public StrokeHistoryAdapter(LayoutHelper layoutHelper, List<String> data) {
        super(layoutHelper, R.layout.item_stroke_history, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item, int position, int offsetTotal) {
    }
}
