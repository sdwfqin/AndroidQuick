package com.sdwfqin.quickseed.ui.components;

import android.content.Intent;
import android.net.Uri;

import com.sdwfqin.quickseed.R;
import com.sdwfqin.quickseed.base.SampleBaseActivity;
import com.sdwfqin.quickseed.model.PictureModel;
import com.sdwfqin.quickseed.utils.picture.PictureSelectUtils;
import com.sdwfqin.widget.pictureupload.PictureUploadCallback;
import com.sdwfqin.widget.pictureupload.PictureUploadView;
import com.zhihu.matisse.Matisse;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 描述：九宫格上传图片
 *
 * @author zhangqin
 * @date 2018/5/31
 */
public class PictureUploadActivity extends SampleBaseActivity {

    public static final int RESULT_PHOTO_SELECT = 101;

    @BindView(R.id.pic)
    PictureUploadView<PictureModel> mPic;

    @Override
    protected int getLayout() {
        return R.layout.activity_picture_upload;
    }

    @Override
    protected void initEventAndData() {
        mTopBar.setTitle("九宫格上传图片");
        mTopBar.addLeftBackImageButton()
                .setOnClickListener(v -> finish());

        mPic.setMaxColumn(3);
        mPic.setPicUploadCallback(new PictureUploadCallback<PictureModel>() {
            @Override
            public void click(int position, PictureModel pictureModel, List<PictureModel> list) {

            }

            @Override
            public void remove(int position, List list) {

            }

            @Override
            public void onAddPic(int maxPic, List list) {
                PictureSelectUtils.SelectSystemPhoto(mContext, RESULT_PHOTO_SELECT, maxPic);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case RESULT_PHOTO_SELECT:
                    List<PictureModel> models = new ArrayList<>();
                    List<String> selectList = Matisse.obtainPathResult(data);
                    for (int i = 0; i < selectList.size(); i++) {
                        models.add(new PictureModel(selectList.get(i)));
                    }
                    mPic.setAddData(models);

                    // 刷新相册图片
                    if (selectList.size() == 1) {
                        String saveAs = selectList.get(0);
                        Uri contentUri = Uri.fromFile(new File(saveAs));
                        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, contentUri);
                        sendBroadcast(mediaScanIntent);
                    }

                    break;
                default:
            }
        }
    }
}
