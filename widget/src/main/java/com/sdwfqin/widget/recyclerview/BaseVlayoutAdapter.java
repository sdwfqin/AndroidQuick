package com.sdwfqin.widget.recyclerview;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.IntRange;
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
import java.util.LinkedHashSet;
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
    private int mCount;
    private LayoutHelper mLayoutHelper;
    private VirtualLayoutManager.LayoutParams mLayoutParams;

    private OnItemClickListener mOnItemClickListener;
    private OnItemChildClickListener mOnItemChildClickListener;
    private final LinkedHashSet<Integer> childClickViewIds;

    public BaseVlayoutAdapter(LayoutHelper layoutHelper, @LayoutRes int layoutResId, List<T> data) {
        this(layoutHelper, layoutResId, data, new VirtualLayoutManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    public BaseVlayoutAdapter(LayoutHelper layoutHelper, @LayoutRes int layoutResId, List<T> data, @NonNull VirtualLayoutManager.LayoutParams layoutParams) {
        this.mLayoutHelper = layoutHelper;
        this.mData = data == null ? new ArrayList<T>() : data;
        this.mLayoutResId = layoutResId;
        this.mLayoutParams = layoutParams;
        this.childClickViewIds = new LinkedHashSet<>();
        this.mCount = mData.size();
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
        bindViewClickListener(holder);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        holder.itemView.setLayoutParams(new VirtualLayoutManager.LayoutParams(mLayoutParams));
    }

    /**
     * @param holder
     * @param position    当前Adapter的位置
     * @param offsetTotal 全局RecyclerView的位置
     */
    @Override
    protected void onBindViewHolderWithOffset(RecyclerView.ViewHolder holder, int position, int offsetTotal) {
        T t;
        try {
            t = mData.get(position);
        } catch (Exception e) {
            t = null;
        }
        convert((BaseViewHolder) holder, t, position, offsetTotal);
    }

    @Override
    public int getItemCount() {
        return mCount;
    }

    private void bindViewClickListener(final BaseViewHolder baseViewHolder) {
        if (baseViewHolder == null) {
            return;
        }
        final View view = baseViewHolder.itemView;
        if (view == null) {
            return;
        }

        if (getOnItemClickListener() != null) {
            view.setOnClickListener(v ->
                    setOnItemClick(v, baseViewHolder.getLayoutPosition()));
        }
    }

    public final OnItemClickListener getOnItemClickListener() {
        return mOnItemClickListener;
    }

    public void setOnItemClick(View v, int position) {
        getOnItemClickListener().onItemClick(BaseVlayoutAdapter.this, v, position);
    }

    public final OnItemChildClickListener getOnItemChildClickListener() {
        return mOnItemChildClickListener;
    }

    public void addOnClickListener(BaseViewHolder baseViewHolder, @IdRes final int viewId) {
        childClickViewIds.add(viewId);
        final View view = baseViewHolder.getView(viewId);
        if (view != null) {
            if (!view.isClickable()) {
                view.setClickable(true);
            }
            view.setOnClickListener(v -> {
                if (getOnItemChildClickListener() != null) {
                    getOnItemChildClickListener()
                            .onItemChildClick(BaseVlayoutAdapter.this, v, baseViewHolder.getLayoutPosition());
                }
            });
        }
    }

    /**
     * 设置Item数量
     *
     * @param count
     */
    public void setItemCount(int count) {
        mCount = count;
        notifyDataSetChanged();
    }

    /**
     * 设置新数据
     *
     * @param data
     */
    public void setNewData(List<T> data) {
        this.mData = data == null ? new ArrayList<T>() : data;
        this.mCount = mData.size();
        notifyDataSetChanged();
    }

    /**
     * 获取数据列表
     *
     * @return 列表数据
     */
    @NonNull
    public List<T> getData() {
        return mData;
    }

    /**
     * Get the data item associated with the specified position in the data set.
     * <p>
     * 当前item中的位置：mDelegateAdapter.findOffsetPosition(position)
     *
     * @param position Position of the item whose data we want within the adapter's
     *                 data set.
     * @return The data at the specified position.
     */
    @Nullable
    public T getItem(@IntRange(from = 0) int position) {
        if (position >= 0 && position < mData.size()) {
            return mData.get(position);
        } else {
            return null;
        }
    }

    public interface OnItemClickListener {

        /**
         * 当前item中的位置：mDelegateAdapter.findOffsetPosition(position)
         */
        void onItemClick(BaseVlayoutAdapter adapter, View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    /**
     * 当前item中的位置：mDelegateAdapter.findOffsetPosition(position)
     */
    public interface OnItemChildClickListener {

        void onItemChildClick(BaseVlayoutAdapter adapter, View view, int position);
    }

    public void setOnItemChildClickListener(OnItemChildClickListener listener) {
        mOnItemChildClickListener = listener;
    }

    /**
     * onBindViewHolder
     *
     * @param helper
     * @param item
     */
    protected abstract void convert(BaseViewHolder helper, T item, int position, int offsetTotal);
}
