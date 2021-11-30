package io.github.sdwfqin.quickseed.ui.components;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.projection.MediaProjectionManager;
import android.os.Build;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.ToastUtils;

import io.github.sdwfqin.quickseed.databinding.ActivityWindowFloatAndScreenshotBinding;
import io.github.sdwfqin.quickseed.services.ScreenRecorderService;
import io.github.sdwfqin.quickseed.view.QuickWindowFloatView;

import io.github.sdwfqin.samplecommonlibrary.base.SampleBaseActivity;
import io.github.sdwfqin.quickseed.constants.ArouterConstants;

/**
 * 悬浮窗与截图Demo
 * <p>
 *
 * @author 张钦
 * @date 2020/4/10
 */
@Route(path = ArouterConstants.COMPONENTS_WINDOWFLOATANDSCREENSHOT)
public class WindowFloatAndScreenshotActivity extends SampleBaseActivity<ActivityWindowFloatAndScreenshotBinding> {

    /**
     * 截图权限
     */
    public static final int REQUEST_MEDIA_PROJECTION = 18;

    @Override
    protected ActivityWindowFloatAndScreenshotBinding getViewBinding() {
        return ActivityWindowFloatAndScreenshotBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void initEventAndData() {
        mNavBar.setTitle("悬浮窗与截图");
        mNavBar.addLeftBackImageButton().setOnClickListener(v -> finish());
    }

    @Override
    protected void initClickListener() {
        mBinding.btnScreenshot.setOnClickListener(v -> {
            initCheckPermissions(new String[]{Manifest.permission.SYSTEM_ALERT_WINDOW}, new OnPermissionCallback() {
                @Override
                public void onSuccess() {
                    requestCapturePermission();
                }

                @Override
                public void onError() {
                    ToastUtils.showShort("悬浮窗权限已被拒绝");
                }
            });
        });
    }

    private void requestCapturePermission() {
        //获取截屏的管理器
        MediaProjectionManager mediaProjectionManager = (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        startActivityForResult(mediaProjectionManager.createScreenCaptureIntent(), REQUEST_MEDIA_PROJECTION);
    }

    private void showFloat(int resultCode, Intent data) {
        QuickWindowFloatView quickWindowFloatView;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            Intent service = new Intent(this, ScreenRecorderService.class);
            service.putExtra("code", resultCode);
            service.putExtra("data", data);
            startForegroundService(service);
            quickWindowFloatView = new QuickWindowFloatView(mContext.getApplication(), null);
        } else {
            quickWindowFloatView = new QuickWindowFloatView(mContext.getApplication(), data);
        }
        quickWindowFloatView.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_MEDIA_PROJECTION) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                showFloat(resultCode, data);
            }
        }
    }
}
