package io.github.sdwfqin.quickseed.ui.example.sortlist;

import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.blankj.utilcode.util.FragmentUtils;
import com.blankj.utilcode.util.LogUtils;

import io.github.sdwfqin.quicklib.base.BaseActivity;
import io.github.sdwfqin.quicklib.utils.rx.RxSchedulersUtils;

import java.util.ArrayList;
import java.util.List;

import io.github.sdwfqin.quickseed.R;
import io.github.sdwfqin.quickseed.databinding.ActivitySortListBinding;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;

/**
 * 仿京东分类列表
 * <p>
 *
 * @author 张钦
 * @date 2020/5/27
 */
public class SortListActivity extends BaseActivity<ActivitySortListBinding> {

    private SortAdapter mSortAdapter;
    private LinearLayoutManager mLayoutManager;

    @Override
    protected ActivitySortListBinding getViewBinding() {
        return ActivitySortListBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void initEventAndData() {
        mNavBar.setTitle("仿京东分类列表");
        mNavBar.addLeftBackImageButton().setOnClickListener(v -> finish());

        initSortList();
    }

    private void initSortList() {
        mSortAdapter = new SortAdapter(getMockData());
        mSortAdapter.setCheckedPosition(0);
        initSortDetail((ArrayList<SortGroupBean>) mSortAdapter.getItem(0).getSortGroupBeans(), 0);
        mLayoutManager = new LinearLayoutManager(mContext);
        mBinding.rvSort.setLayoutManager(mLayoutManager);
        mBinding.rvSort.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));
        mBinding.rvSort.setAdapter(mSortAdapter);

        mSortAdapter.setOnItemClickListener((adapter, view, position) -> {
            mSortAdapter.setCheckedPosition(position);
        });
        mSortAdapter.setOnCallbackListener(position -> {
            if (position >= mSortAdapter.getData().size()) {
                return;
            }
            initSortDetail((ArrayList<SortGroupBean>) mSortAdapter.getItem(position).getSortGroupBeans(), position);
            try {
                View childAt = mBinding.rvSort.getChildAt(position - mLayoutManager.findFirstVisibleItemPosition());
                int y = (childAt.getTop() - mBinding.rvSort.getHeight() / 2);
                mBinding.rvSort.smoothScrollBy(0, y);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void initSortDetail(ArrayList<SortGroupBean> sortGroupBean, int positions) {
        if (sortGroupBean == null) {
            return;
        }
        // 在子线程加载Fragment
        addSubscribe(Observable.create((ObservableOnSubscribe<Fragment>) emitter -> {
            int witchType = SortListDetailFragment.ALL;
            if (mSortAdapter.getData().size() == 1) {
                witchType = SortListDetailFragment.NONE;
            } else if (positions == 0) {
                witchType = SortListDetailFragment.BACK;
            } else if (mSortAdapter.getData().size() - 1 == positions) {
                witchType = SortListDetailFragment.NEXT;
            }
            SortListDetailFragment sortListDetailFragment = SortListDetailFragment.newInstance(sortGroupBean, witchType);
            emitter.onNext(sortListDetailFragment);
            emitter.onComplete();
        }).compose(RxSchedulersUtils.rxObservableSchedulerHelper())
                .subscribe(fragment -> FragmentUtils.replace(getSupportFragmentManager(), fragment, R.id.fl_sort_detail),
                        LogUtils::e));
    }

    public void backSort() {
        int checkedPosition = mSortAdapter.getCheckedPosition();
        if (checkedPosition <= 0) {
            return;
        }
        mSortAdapter.setCheckedPosition(--checkedPosition);
    }

    public void nextSort() {
        int checkedPosition = mSortAdapter.getCheckedPosition();
        if (checkedPosition >= mSortAdapter.getData().size() - 1) {
            return;
        }
        mSortAdapter.setCheckedPosition(++checkedPosition);
    }

    private List<SortBean> getMockData() {
        List<SortBean> beans = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            SortBean sortBean = new SortBean();
            sortBean.setId(i);
            sortBean.setTitle("分类：" + i);
            List<SortGroupBean> groupBeans = new ArrayList<>();
            for (int j = 0; j < 5; j++) {
                SortGroupBean sortGroupBean = new SortGroupBean();
                sortGroupBean.setId(i + j);
                sortGroupBean.setTitle("分组：" + i + j);
                List<SortDetailBean> detailBeans = new ArrayList<>();
                for (int k = 0; k < 9; k++) {
                    SortDetailBean sortDetailBean = new SortDetailBean();
                    sortDetailBean.setId(i + j + k);
                    sortDetailBean.setImgUrl("test");
                    sortDetailBean.setTitle("数据：" + i + j + k);
                    detailBeans.add(sortDetailBean);
                }
                sortGroupBean.setSortDetailBeans(detailBeans);
                groupBeans.add(sortGroupBean);
            }
            sortBean.setSortGroupBeans(groupBeans);
            beans.add(sortBean);
        }

        return beans;
    }
}
