package io.github.sdwfqin.quickseed.ui.components;

import com.alibaba.android.arouter.facade.annotation.Route;
import io.github.sdwfqin.quickseed.constants.ArouterConstants;
import io.github.sdwfqin.quickseed.databinding.ActivityCircleProgressDemoBinding;
import io.github.sdwfqin.samplecommonlibrary.base.SampleBaseActivity;

/**
 * 圆（方）形加载进度条
 * <p>
 *
 * @author 张钦
 * @date 2020/4/21
 */
@Route(path = ArouterConstants.COMPONENTS_CIRCLEPROGRESSDEMO)
public class CircleProgressDemoActivity extends SampleBaseActivity<ActivityCircleProgressDemoBinding> {

    @Override
    protected ActivityCircleProgressDemoBinding getViewBinding() {
        return ActivityCircleProgressDemoBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void initEventAndData() {
        mTopBar.setTitle("圆（方）形加载进度条");
        mTopBar.addLeftBackImageButton().setOnClickListener(v -> finish());

        mBinding.test.setOnClickListener(v -> {
            mBinding.progressBar1.setProgress(mBinding.progressBar1.getProgress() + 2);
            mBinding.progressBar2.setProgress(mBinding.progressBar2.getProgress() + 2);
            mBinding.progressBar3.setProgress(mBinding.progressBar3.getProgress() + 2);
            mBinding.progressBar4.setProgress(mBinding.progressBar4.getProgress() + 2);
        });
    }
}
