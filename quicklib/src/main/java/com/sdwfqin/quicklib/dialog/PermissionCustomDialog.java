package com.sdwfqin.quicklib.dialog;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ScreenUtils;
import com.permissionx.guolindev.request.RationaleDialog;
import com.qmuiteam.qmui.skin.QMUISkinManager;
import com.sdwfqin.quicklib.R;
import com.sdwfqin.quicklib.databinding.QuickDialogPermissionBinding;
import com.sdwfqin.quicklib.databinding.QuickItemPermissionsBinding;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 自定义权限弹窗
 * <p>
 *
 * @author 张钦
 * @date 2020/7/23
 */
public class PermissionCustomDialog extends RationaleDialog {

    private Map<String, String> permissionMap = new HashMap<>();
    private Set<String> groupSet = new HashSet<>();
    private String message;
    private List<String> permissions;
    private QuickDialogPermissionBinding mBinding;

    {
        permissionMap.put(Manifest.permission.READ_CALENDAR, Manifest.permission_group.CALENDAR);
        permissionMap.put(Manifest.permission.WRITE_CALENDAR, Manifest.permission_group.CALENDAR);
        permissionMap.put(Manifest.permission.READ_CALL_LOG, Manifest.permission_group.CALL_LOG);
        permissionMap.put(Manifest.permission.WRITE_CALL_LOG, Manifest.permission_group.CALL_LOG);
        permissionMap.put(Manifest.permission.PROCESS_OUTGOING_CALLS, Manifest.permission_group.CALL_LOG);
        permissionMap.put(Manifest.permission.CAMERA, Manifest.permission_group.CAMERA);
        permissionMap.put(Manifest.permission.READ_CONTACTS, Manifest.permission_group.CONTACTS);
        permissionMap.put(Manifest.permission.WRITE_CONTACTS, Manifest.permission_group.CONTACTS);
        permissionMap.put(Manifest.permission.GET_ACCOUNTS, Manifest.permission_group.CONTACTS);
        permissionMap.put(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission_group.LOCATION);
        permissionMap.put(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission_group.LOCATION);
        permissionMap.put(Manifest.permission.ACCESS_BACKGROUND_LOCATION, Manifest.permission_group.LOCATION);
        permissionMap.put(Manifest.permission.RECORD_AUDIO, Manifest.permission_group.MICROPHONE);
        permissionMap.put(Manifest.permission.READ_PHONE_STATE, Manifest.permission_group.PHONE);
        permissionMap.put(Manifest.permission.READ_PHONE_NUMBERS, Manifest.permission_group.PHONE);
        permissionMap.put(Manifest.permission.CALL_PHONE, Manifest.permission_group.PHONE);
        permissionMap.put(Manifest.permission.ANSWER_PHONE_CALLS, Manifest.permission_group.PHONE);
        permissionMap.put(Manifest.permission.ADD_VOICEMAIL, Manifest.permission_group.PHONE);
        permissionMap.put(Manifest.permission.USE_SIP, Manifest.permission_group.PHONE);
        permissionMap.put(Manifest.permission.ACCEPT_HANDOVER, Manifest.permission_group.PHONE);
        permissionMap.put(Manifest.permission.BODY_SENSORS, Manifest.permission_group.SENSORS);
        permissionMap.put(Manifest.permission.ACTIVITY_RECOGNITION, Manifest.permission_group.ACTIVITY_RECOGNITION);
        permissionMap.put(Manifest.permission.SEND_SMS, Manifest.permission_group.SMS);
        permissionMap.put(Manifest.permission.RECEIVE_SMS, Manifest.permission_group.SMS);
        permissionMap.put(Manifest.permission.READ_SMS, Manifest.permission_group.SMS);
        permissionMap.put(Manifest.permission.RECEIVE_WAP_PUSH, Manifest.permission_group.SMS);
        permissionMap.put(Manifest.permission.RECEIVE_MMS, Manifest.permission_group.SMS);
        permissionMap.put(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission_group.STORAGE);
        permissionMap.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission_group.STORAGE);
        permissionMap.put(Manifest.permission.ACCESS_MEDIA_LOCATION, Manifest.permission_group.STORAGE);
    }

    public PermissionCustomDialog(@NonNull Context context, String message, List<String> permissions) {
        super(context, R.style.transactionDialog);
        this.message = message;
        this.permissions = permissions;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = QuickDialogPermissionBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        mBinding.messageText.setText(message);
        buildPermissionsLayout();
        if (getWindow() != null) {
            int width = (int) (ScreenUtils.getScreenWidth() * 0.8);
            getWindow()
                    .setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT);
            getWindow().setGravity(Gravity.CENTER);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        QMUISkinManager.defaultInstance(getContext()).register(this);
    }

    private void buildPermissionsLayout() {
        for (String permission : permissions) {
            String permissionGroup = permissionMap.get(permission);
            if (permissionGroup != null && !groupSet.contains(permissionGroup)) {
                QuickItemPermissionsBinding permissionsItemBinding = QuickItemPermissionsBinding.inflate(getLayoutInflater());
                try {
                    permissionsItemBinding.bodyItem.setText(getContext().getPackageManager().getPermissionGroupInfo(permissionGroup, 0).loadLabel(getContext().getPackageManager()));
                    mBinding.permissionsLayout.addView(permissionsItemBinding.getRoot());
                    groupSet.add(permissionGroup);
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @NonNull
    @Override
    public View getPositiveButton() {
        return mBinding.positiveBtn;
    }

    @Nullable
    @Override
    public View getNegativeButton() {
        return mBinding.negativeBtn;
    }

    @NonNull
    @Override
    public List<String> getPermissionsToRequest() {
        return permissions;
    }
}
