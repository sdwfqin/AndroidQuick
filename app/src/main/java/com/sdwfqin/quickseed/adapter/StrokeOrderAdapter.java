package com.sdwfqin.quickseed.adapter;

import com.alibaba.android.vlayout.LayoutHelper;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.sdwfqin.quickseed.R;
import com.sdwfqin.widget.recyclerview.BaseVlayoutAdapter;

import java.util.List;

/**
 * 匹配行程订单
 * <p>
 *
 * @author 张钦
 * @date 2019-06-26
 */
public class StrokeOrderAdapter extends BaseVlayoutAdapter<String> {

    public StrokeOrderAdapter(LayoutHelper layoutHelper, List<String> data) {
        super(layoutHelper, R.layout.item_stroke_order, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item, int position, int offsetTotal) {
        if (position == 0) {
            helper.setVisible(R.id.v_hint_top, false);
        } else {
            helper.setVisible(R.id.v_hint_top, true);
        }
    }
}
