package com.sdwfqin.quickseed.ui.components;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sdwfqin.quickseed.R;
import com.sdwfqin.quickseed.adapter.AutoPollRecyclerAdapter;
import com.sdwfqin.quickseed.base.SampleBaseActivity;
import com.sdwfqin.widget.recyclerview.AutoPollRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

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
public class AutoPollRecyclerViewActivity extends SampleBaseActivity {

    @BindView(R.id.list_v)
    AutoPollRecyclerView mListV;
    @BindView(R.id.list_h)
    AutoPollRecyclerView mListH;

    @Override
    protected int getLayout() {
        return R.layout.activity_auto_poll_recycler_view;
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
        mListV.setLayoutManager(new LinearLayoutManager(mContext));
        mListV.setAdapter(adapterv);
        mListV.start();

        AutoPollRecyclerAdapter adapterh = new AutoPollRecyclerAdapter(R.layout.item_autopoll_h, list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        mListH.setLayoutManager(linearLayoutManager);
        mListH.setAdapter(adapterh);
        mListH.start();
    }
}
