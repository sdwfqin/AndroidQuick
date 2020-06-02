package com.sdwfqin.quickseed.ui.example.sortlist;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.sdwfqin.quickseed.R;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SortDetailAdapter extends BaseMultiItemQuickAdapter<SortDetailBean, BaseViewHolder>{

    public static final int GROUP = 1;
    public static final int DETAIL = 2;

    public SortDetailAdapter(@Nullable List<SortDetailBean> data) {
        super(data);

        addItemType(GROUP, R.layout.item_sort_group);
        addItemType(DETAIL, R.layout.item_sort_detail);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, SortDetailBean item) {
        switch (baseViewHolder.getItemViewType()) {
            case GROUP:
                baseViewHolder.setText(R.id.tv_title, item.getTitle());
                break;
            case DETAIL:
                baseViewHolder.setText(R.id.tv_title, item.getTitle());
                break;
            default:
                break;
        }
    }
}
