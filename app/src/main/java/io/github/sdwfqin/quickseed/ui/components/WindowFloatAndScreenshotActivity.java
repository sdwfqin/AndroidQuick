package io.github.sdwfqin.quickseed.ui.components;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.ToastUtils;

import io.github.sdwfqin.quickseed.databinding.ActivityWindowFloatAndScreenshotBinding;
import io.github.sdwfqin.samplecommonlibrary.view.QuickWindowFloatView;

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
    /**
     * 悬浮窗
     */
    public static final int REQUEST_ALERT = 19;

    private MediaProjectionManager mMediaProjectionManager;

    @Override
    protected ActivityWindowFloatAndScreenshotBinding getViewBinding() {
        return ActivityWindowFloatAndScreenshotBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void initEventAndData() {
        mTopBar.setTitle("悬浮窗与截图");
        mTopBar.addLeftBackImageButton().setOnClickListener(v -> finish());
    }

    @Override
    protected void initClickListener() {
        mBinding.btnScreenshot.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!Settings.canDrawOverlays(getApplicationContext())) {
                    //启动Activity让用户授权
                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                    intent.setData(Uri.parse("package:" + getPackageName()));
                    startActivityForResult(intent, REQUEST_ALERT);
                } else {
                    requestCapturePermission();
                }
            } else {
                requestCapturePermission();
            }
        });
    }

    private void requestCapturePermission() {
        //获取截屏的管理器
        mMediaProjectionManager = (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        startActivityForResult(mMediaProjectionManager.createScreenCaptureIntent(), REQUEST_MEDIA_PROJECTION);
    }

    private void showFloat(Intent data) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ToastUtils.showShort("当前功能尚未适配Android 10，后续空闲会修改！");
            return;
        }
        QuickWindowFloatView quickWindowFloatView = new QuickWindowFloatView(mContext, data);
        quickWindowFloatView.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_MEDIA_PROJECTION:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    showFloat(data);
                }
                break;
            case REQUEST_ALERT:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (Settings.canDrawOverlays(this)) {
                        requestCapturePermission();
                    } else {
                        ToastUtils.showShort("ACTION_MANAGE_OVERLAY_PERMISSION权限已被拒绝");
                    }
                }
        }
    }
}
