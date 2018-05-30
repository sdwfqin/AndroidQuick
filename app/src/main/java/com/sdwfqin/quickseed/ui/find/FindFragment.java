package com.sdwfqin.quickseed.ui.find;

import android.os.Build;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sdwfqin.quicklib.base.BaseFragment;
import com.sdwfqin.quickseed.R;
import com.sdwfqin.quickseed.base.Constants;

import butterknife.BindView;

public class FindFragment extends BaseFragment {

    @BindView(R.id.status_view)
    View mStatusView;
    @BindView(R.id.home_msg)
    ImageView mHomeMsg;
    @BindView(R.id.home_title_tv)
    TextView mHomeTitleTv;

    @Override
    protected int getLayout() {
        return R.layout.fragment_find;
    }

    @Override
    protected void initEventAndData() {

        if (Build.VERSION.SDK_INT < 19) {
            mStatusView.setVisibility(View.GONE);
        } else {
            mStatusView.getLayoutParams().height = Constants.STATUS_HEIGHT;
        }

        mHomeTitleTv.setText("发现");

    }

    @Override
    protected void lazyLoadShow(boolean isLoad) {

    }
}
