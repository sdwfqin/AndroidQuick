package io.github.sdwfqin.quickseed.adapter;

import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import io.github.sdwfqin.quickseed.R;
import io.github.sdwfqin.widget.recyclerview.BaseAutoPollAdapter;

import java.util.List;

/**
 * 标题：跑马灯Demo
 * 详细描述：
 * <p>
 * 创建者：张钦
 * 创建时间：2019/4/14
 * <p>
 * 修改者：张钦
 * 修改时间：2019/4/14
 * 修改内容：
 */
public class AutoPollRecyclerAdapter extends BaseAutoPollAdapter<String> {

    public AutoPollRecyclerAdapter(int layoutResId,List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.text, item);
    }
}
