package io.github.sdwfqin.quickseed.ui.components;

import android.graphics.drawable.PictureDrawable;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.bumptech.glide.RequestBuilder;
import io.github.sdwfqin.imageloader.GlideApp;

import io.github.sdwfqin.imageloader.svg.SvgSoftwareLayerSetter;
import io.github.sdwfqin.quickseed.databinding.ActivityShowSvgBinding;
import io.github.sdwfqin.samplecommonlibrary.base.SampleBaseActivity;
import io.github.sdwfqin.quickseed.constants.ArouterConstants;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

/**
 * 展示Svg图片
 * <p>
 *
 * @author 张钦
 * @date 2019-10-29
 */
@Route(path = ArouterConstants.COMPONENTS_SHOWSVG)
public class ShowSvgActivity extends SampleBaseActivity<ActivityShowSvgBinding> {

    private String mImgRes =
            "<?xml version=\"1.0\" standalone=\"no\"?>\n" +
                    "<!DOCTYPE svg PUBLIC \"-//W3C//DTD SVG 1.1//EN\" \"http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd\">\n" +
                    "<svg version=\"1.1\" id=\"图层_1\" xmlns=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" x=\"0px\" y=\"0px\" viewBox=\"0 0 800 442.7\" style=\"enable-background:new 0 0 800 442.7;\" xml:space=\"preserve\">\n" +
                    "<defs>\n" +
                    "<style type=\"text/css\">\n" +
                    "\t.st0{fill:#FF0000;stroke:#000000;stroke-miterlimit:10;}\n" +
                    "\t.st1{fill:#FFFFFF;stroke:#000000;stroke-miterlimit:10;}\n" +
                    "\t.st2{font-family:'AdobeSongStd-Light-GBpc-EUC-H';}\n" +
                    "\t.st3{font-size:14px;}\n" +
                    "</style>\n" +
                    "</defs>\n" +
                    "<g fill=\"#000000\">\n" +
                    "\t<rect x=\"21.6\" y=\"29.3\" class=\"st0\" width=\"126.9\" height=\"53.8\"/>\n" +
                    "\t<rect x=\"148.4\" y=\"29.3\" class=\"st1\" width=\"126.9\" height=\"53.8\"/>\n" +
                    "</g>\n" +
                    "<g id=\"文字图层\">\n" +
                    "\t<text transform=\"matrix(1 0 0 1 65.7729 63.0264)\" class=\"st2 st3\">测试1</text>\n" +
                    "\t<text transform=\"matrix(1 0 0 1 188.7477 63.4633)\" class=\"st2 st3\">测试2</text>\n" +
                    "</g>\n" +
                    "</svg>";

    @Override
    protected ActivityShowSvgBinding getViewBinding() {
        return ActivityShowSvgBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void initEventAndData() {

        mNavBar.setTitle("展示原生SVG图片");
        mNavBar.addLeftBackImageButton()
                .setOnClickListener(v -> finish());

        RequestBuilder<PictureDrawable> requestBuilder =
                GlideApp.with(this)
                        .as(PictureDrawable.class)
                        .transition(withCrossFade())
                        .listener(new SvgSoftwareLayerSetter());
        requestBuilder
                .load(mImgRes.getBytes())
                .into(mBinding.img);

    }

    @Override
    protected void initClickListener() {

    }

}
