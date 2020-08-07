package com.sdwfqin.quickseed.ui.components;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.sdwfqin.quickseed.R;
import com.sdwfqin.quickseed.adapter.AutoPollRecyclerAdapter;
import com.sdwfqin.quickseed.constants.ArouterConstants;
import com.sdwfqin.quickseed.databinding.ActivityAutoPollRecyclerViewBinding;

import java.util.ArrayList;
import java.util.List;

import io.github.sdwfqin.samplecommonlibrary.base.SampleBaseActivity;

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
@Route(path = ArouterConstants.COMPONENTS_AUTOPOLLRECYCLER)
public class AutoPollRecyclerViewActivity extends SampleBaseActivity<ActivityAutoPollRecyclerViewBinding> {

    @Override
    protected ActivityAutoPollRecyclerViewBinding getViewBinding() {
        return ActivityAutoPollRecyclerViewBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void initEventAndData() {

        mTopBar.addLeftBackImageButton().setOnClickListener(v -> finish());
        mTopBar.setTitle("跑马灯Demo");

        List<String> list = new ArrayList<>();
        for (int i = 0; i < 10; ) {
            list.add(" Item: " + ++i);
        }

        AutoPollRecyclerAdapter adapterv = new AutoPollRecyclerAdapter(R.layout.item_autopoll_v, list);
        mBinding.listV.setLayoutManager(new LinearLayoutManager(mContext));
        mBinding.listV.setAdapter(adapterv);
        mBinding.listV.start();

        AutoPollRecyclerAdapter adapterh = new AutoPollRecyclerAdapter(R.layout.item_autopoll_h, list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        mBinding.listH.setLayoutManager(linearLayoutManager);
        mBinding.listH.setAdapter(adapterh);
        mBinding.listH.start();
    }

    @Override
    protected void initClickListener() {

    }
}
