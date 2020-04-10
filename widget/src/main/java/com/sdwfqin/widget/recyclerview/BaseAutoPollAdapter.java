package com.sdwfqin.widget.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：自动滚动的RecyclerView的adapter基类
 *
 * @author zhangqin
 * @date 2018/3/15
 */
public abstract class BaseAutoPollAdapter<T> extends RecyclerView.Adapter<BaseViewHolder> {

    private Context mContext;
    private List<T> mData;
    private int mLayoutResId;

    public BaseAutoPollAdapter(@LayoutRes int layoutResId, List<T> data) {
        this.mData = data == null ? new ArrayList<T>() : data;
        this.mLayoutResId = layoutResId;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(mLayoutResId, parent, false);
        return new BaseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        convert(holder, mData.get(position % mData.size()));
    }

    @Override
    public int getItemCount() {
        return Integer.MAX_VALUE;
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
    protected abstract void convert(BaseViewHolder helper, T item);
}
