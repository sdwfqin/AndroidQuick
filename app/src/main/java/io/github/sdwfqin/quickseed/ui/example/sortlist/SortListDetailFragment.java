package io.github.sdwfqin.quickseed.ui.example.sortlist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.scwang.smart.refresh.header.ClassicsHeader;
import io.github.sdwfqin.quicklib.base.BaseFragment;
import io.github.sdwfqin.quickseed.databinding.FragmentSortListDetailBinding;
import io.github.sdwfqin.quickseed.databinding.ItemSortFooterBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * 仿京东分类列表 -> 分类详情
 *
 * @author 张钦
 * @date 2020/5/27
 */
public class SortListDetailFragment extends BaseFragment<FragmentSortListDetailBinding> {

    public static final int ALL = 0;
    public static final int BACK = 1;
    public static final int NEXT = 2;
    public static final int NONE = 3;

    private SortDetailAdapter mSortDetailAdapter;
    private boolean isBottom = false;
    // 0普通，1不能向上切换，2不能向下切换，3不能切换
    private int mSwitchType;

    public static SortListDetailFragment newInstance(ArrayList<SortGroupBean> sortGroupBean, int switchType) {

        Bundle args = new Bundle();

        SortListDetailFragment fragment = new SortListDetailFragment();
        args.putParcelableArrayList("data", sortGroupBean);
        args.putInt("switchType", switchType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected FragmentSortListDetailBinding getViewBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return FragmentSortListDetailBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void initEventAndData() {

        // TODO：上面的header应该是要重写的
        ClassicsHeader.REFRESH_HEADER_PULLING = "下拉切换";
        ClassicsHeader.REFRESH_HEADER_RELEASE = "释放立即切换";
        ClassicsHeader.REFRESH_HEADER_REFRESHING = "正在加载";
        ClassicsHeader.REFRESH_HEADER_UPDATE = "";

        mSwitchType = getArguments().getInt("switchType", ALL);

        mBinding.srl.setEnableLoadMore(false);
        if (mSwitchType == BACK || mSwitchType == NONE) {
            mBinding.srl.setEnableRefresh(false);
        }
        mBinding.srl.setOnRefreshListener(refreshLayout -> {
            ((SortListActivity) getActivity()).backSort();
        });

        List<SortDetailBean> detailBeans = new ArrayList<>();
        List<SortGroupBean> datas = getArguments().getParcelableArrayList("data");
        for (int i = 0; i < datas.size(); i++) {
            SortDetailBean sortDetailBean = new SortDetailBean();
            sortDetailBean.setGroupTitle(true);
            sortDetailBean.setTitle(datas.get(i).getTitle());
            detailBeans.add(sortDetailBean);
            detailBeans.addAll(datas.get(i).getSortDetailBeans());
        }

        mSortDetailAdapter = new SortDetailAdapter(detailBeans);

        if (mSwitchType != NEXT && mSwitchType != NONE) {
            ItemSortFooterBinding footerView = ItemSortFooterBinding.inflate(getLayoutInflater());
            mSortDetailAdapter.addFooterView(footerView.getRoot());
        }

        GridLayoutManager manager = new GridLayoutManager(getContext(), 3);
        mBinding.rvDetail.setLayoutManager(manager);
        mBinding.rvDetail.addItemDecoration(new SortGroupItemDecoration(getContext()));
        mBinding.rvDetail.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (mSwitchType == NEXT || mSwitchType == NONE) {
                    return;
                }
                // false表示已经滚动到底部
                boolean scrollVertically = mBinding.rvDetail.canScrollVertically(1);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && !scrollVertically) {
                    if (isBottom) {
                        ((SortListActivity) getActivity()).nextSort();
                    }
                    isBottom = true;
                } else if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    isBottom = false;
                }
            }
        });
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return mSortDetailAdapter.getItem(position).isGroupTitle() ? 3 : 1;
            }
        });
        mBinding.rvDetail.setAdapter(mSortDetailAdapter);
    }
}
