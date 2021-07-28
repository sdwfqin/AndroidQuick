package io.github.sdwfqin.quickseed.adapter;

import com.alibaba.android.vlayout.LayoutHelper;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import io.github.sdwfqin.quickseed.R;
import io.github.sdwfqin.widget.recyclerview.BaseVlayoutAdapter;

import java.util.List;

/**
 * 待出行行程卡片
 * <p>
 *
 * @author 张钦
 * @date 2019-06-26
 */
public class StrokeCardAdapter extends BaseVlayoutAdapter<String> {

    public StrokeCardAdapter(LayoutHelper layoutHelper, List<String> data) {
        super(layoutHelper, R.layout.item_stroke_card_me, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item, int position, int offsetTotal) {

    }
}
