package io.github.sdwfqin.widget.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.IdRes;
import androidx.annotation.IntRange;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：DelegateAdapter建议封装
 *
 * @author zhangqin
 * @date 2018/3/16
 */
public abstract class BaseVlayoutAdapter<T> extends DelegateAdapter.Adapter<BaseViewHolder> {

    protected Context mContext;
    private List<T> mData;
    private int mLayoutResId;
    private int mCount;
    private LayoutHelper mLayoutHelper;
    private VirtualLayoutManager.LayoutParams mLayoutParams;

    private OnItemClickListener mOnItemClickListener;
    private OnItemChildClickListener mOnItemChildClickListener;

    public BaseVlayoutAdapter(LayoutHelper layoutHelper, @LayoutRes int layoutResId, List<T> data) {
        this(layoutHelper, layoutResId, data, new VirtualLayoutManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    public BaseVlayoutAdapter(LayoutHelper layoutHelper, @LayoutRes int layoutResId, List<T> data, @NonNull VirtualLayoutManager.LayoutParams layoutParams) {
        this.mLayoutHelper = layoutHelper;
        this.mData = data == null ? new ArrayList<>() : data;
        this.mLayoutResId = layoutResId;
        this.mLayoutParams = layoutParams;
        this.mCount = mData.size();
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return mLayoutHelper;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(mLayoutResId, parent, false);
        return new BaseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.itemView.setLayoutParams(new VirtualLayoutManager.LayoutParams(mLayoutParams));
    }

    /**
     * @param holder
     * @param position    当前Adapter的位置
     * @param offsetTotal 全局RecyclerView的位置
     */
    @Override
    protected void onBindViewHolderWithOffset(BaseViewHolder holder, int position, int offsetTotal) {
        T t;
        try {
            t = mData.get(position);
        } catch (Exception e) {
            t = null;
        }
        if (mContext == null || t == null) {
            return;
        }
        convert(holder, t, position, offsetTotal);
        bindViewClickListener(holder, position);
    }

    @Override
    public int getItemCount() {
        return mCount;
    }

    private void bindViewClickListener(BaseViewHolder baseViewHolder, final int position) {
        if (baseViewHolder == null) {
            return;
        }
        if (getOnItemClickListener() != null) {
            baseViewHolder.itemView.setOnClickListener(view -> setOnItemClick(view, position));
        }
    }

    public final OnItemClickListener getOnItemClickListener() {
        return mOnItemClickListener;
    }

    public void setOnItemClick(View v, int position) {
        getOnItemClickListener().onItemClick(this, v, position);
    }

    public final OnItemChildClickListener getOnItemChildClickListener() {
        return mOnItemChildClickListener;
    }

    public void addOnClickListener(final BaseViewHolder baseViewHolder, @IdRes final int viewId, final int position) {
        View view = baseViewHolder.getView(viewId);
        if (!view.isClickable()) {
            view.setClickable(true);
        }
        view.setOnClickListener(view1 -> {
            if (getOnItemChildClickListener() != null) {
                getOnItemChildClickListener()
                        .onItemChildClick(BaseVlayoutAdapter.this, view1, position);
            }
        });
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
     * 添加新数据
     *
     * @param data
     */
    public void addData(List<T> data) {
        this.mData.addAll(data == null ? new ArrayList<T>() : data);
        this.mCount = mData.size();
        notifyDataSetChanged();
    }

    /**
     * 插入新数据
     *
     * @param data
     */
    public void insertItemData(int pos, T data) {
        this.mData.add(pos, data);
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
