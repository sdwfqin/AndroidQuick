package com.sdwfqin.quicklib.view.recyclerview;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：DelegateAdapter建议封装
 *
 * @author zhangqin
 * @date 2018/3/16
 */
public abstract class BaseVlayoutAdapter<T> extends DelegateAdapter.Adapter {

    protected Context mContext;
    private List<T> mData;
    private int mLayoutResId;
    private LayoutHelper mLayoutHelper;
    private VirtualLayoutManager.LayoutParams mLayoutParams;

    public BaseVlayoutAdapter(LayoutHelper layoutHelper, @LayoutRes int layoutResId, List<T> data) {
        this(layoutHelper, layoutResId, data, new VirtualLayoutManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    public BaseVlayoutAdapter(LayoutHelper layoutHelper, @LayoutRes int layoutResId, List<T> data, @NonNull VirtualLayoutManager.LayoutParams layoutParams) {
        this.mLayoutHelper = layoutHelper;
        this.mData = data == null ? new ArrayList<T>() : data;
        this.mLayoutResId = layoutResId;
        this.mLayoutParams = layoutParams;
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return mLayoutHelper;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(mLayoutResId, parent, false);
        BaseViewHolder holder = new BaseViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        holder.itemView.setLayoutParams(new VirtualLayoutManager.LayoutParams(mLayoutParams));
    }

    @Override
    protected void onBindViewHolderWithOffset(RecyclerView.ViewHolder holder, int position, int offsetTotal) {
        convert((BaseViewHolder) holder, mData.get(offsetTotal), position, offsetTotal);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public void setNewData(@Nullable List<T> data) {
        this.mData = data == null ? new ArrayList<T>() : data;
        notifyDataSetChanged();
    }

    /**
     * onBindViewHolder
     *
     * @param helper
     * @param item
     */
    protected abstract void convert(BaseViewHolder helper, T item, int position, int offsetTotal);
}
