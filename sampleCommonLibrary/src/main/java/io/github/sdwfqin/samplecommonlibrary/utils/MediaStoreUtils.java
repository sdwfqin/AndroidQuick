package io.github.sdwfqin.samplecommonlibrary.utils;

import android.content.ContentValues;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.PathUtils;

import java.io.File;
import java.util.UUID;

public class MediaStoreUtils {

    public static ContentValues getImageContentValues() {
        String fileName = UUID.randomUUID().toString() + ".jpg";
        String filePath = PathUtils.getExternalPicturesPath() + File.separator + AppUtils.getAppName();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            filePath = Environment.DIRECTORY_PICTURES + File.separator + AppUtils.getAppName();
        }
        LogUtils.e(filePath);
        return getImageContentValues("image/jpeg", filePath, fileName);
    }

    public static ContentValues getImageContentValues(String mimeType, String filePath, String fileName) {
        long timeMillis = System.currentTimeMillis();

        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName);
        contentValues.put(MediaStore.MediaColumns.TITLE, fileName);
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, mimeType);
        contentValues.put(MediaStore.MediaColumns.DATE_ADDED, timeMillis);
        contentValues.put(MediaStore.MediaColumns.DATE_MODIFIED, timeMillis);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, filePath);
            contentValues.put(MediaStore.MediaColumns.DATE_TAKEN, timeMillis);
        } else {
            contentValues.put(MediaStore.MediaColumns.DATA, filePath + File.separator + fileName);
        }
        return contentValues;
    }
}
