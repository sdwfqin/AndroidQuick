package com.sdwfqin.quickseed.ui;

import android.content.Intent;

import com.sdwfqin.quicklib.base.BaseActivity;
import com.sdwfqin.quickseed.R;
import com.sdwfqin.quickseed.base.Constants;
import com.sdwfqin.quickseed.utils.picture.PictureSelectUtils;
import com.sdwfqin.widget.pictureupload.PictureUploadCallback;
import com.sdwfqin.widget.pictureupload.PictureUploadView;
import com.zhihu.matisse.Matisse;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 描述：九宫格上传图片
 *
 * @author zhangqin
 * @date 2018/5/31
 */
public class PictureUploadActivity extends BaseActivity {

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
                PictureSelectUtils.SelectSystemPhoto(mContext, Constants.RESULT_CODE_1, maxPic);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1) {
            switch (requestCode) {
                case Constants.RESULT_CODE_1:
                    List<PictureModel> models = new ArrayList<>();
                    List<String> selectList = Matisse.obtainPathResult(data);
                    for (int i = 0; i < selectList.size(); i++) {
                        models.add(new PictureModel(selectList.get(i)));
                    }
                    mPic.setAddData(models);
                    break;
                default:
            }
        }
    }
}
