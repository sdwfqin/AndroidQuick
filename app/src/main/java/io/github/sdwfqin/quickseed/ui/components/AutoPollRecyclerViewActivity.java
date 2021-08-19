package io.github.sdwfqin.quickseed.ui.components;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;

import io.github.sdwfqin.quickseed.R;
import io.github.sdwfqin.quickseed.adapter.AutoPollRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

import io.github.sdwfqin.quickseed.databinding.ActivityAutoPollRecyclerViewBinding;
import io.github.sdwfqin.samplecommonlibrary.base.SampleBaseActivity;
import io.github.sdwfqin.quickseed.constants.ArouterConstants;

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

        mNavBar.addLeftBackImageButton().setOnClickListener(v -> finish());
        mNavBar.setTitle("跑马灯Demo");

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
