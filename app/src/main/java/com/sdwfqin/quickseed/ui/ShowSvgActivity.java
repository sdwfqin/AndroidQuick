package com.sdwfqin.quickseed.ui;

import android.graphics.drawable.PictureDrawable;
import android.widget.ImageView;

import com.bumptech.glide.RequestBuilder;
import com.sdwfqin.imageloader.GlideApp;
import com.sdwfqin.imageloader.svg.SvgSoftwareLayerSetter;
import com.sdwfqin.quickseed.R;
import com.sdwfqin.quickseed.base.SampleBaseActivity;

import butterknife.BindView;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

/**
 * 展示Svg图片
 * <p>
 *
 * @author 张钦
 * @date 2019-10-29
 */
public class ShowSvgActivity extends SampleBaseActivity {

    @BindView(R.id.img)
    ImageView mImg;

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
                    "\t<rect x=\"274.5\" y=\"29.3\" class=\"st1\" width=\"126.9\" height=\"53.8\"/>\n" +
                    "\t<rect x=\"480.6\" y=\"29.3\" class=\"st1\" width=\"126.9\" height=\"53.8\"/>\n" +
                    "\t<rect x=\"633.9\" y=\"29.3\" class=\"st1\" width=\"126.9\" height=\"53.8\"/>\n" +
                    "\t<rect x=\"21.6\" y=\"112.5\" class=\"st1\" width=\"199.2\" height=\"53.8\"/>\n" +
                    "\t<rect x=\"21.6\" y=\"166.3\" class=\"st1\" width=\"199.2\" height=\"53.8\"/>\n" +
                    "\t<rect x=\"21.6\" y=\"220.1\" class=\"st1\" width=\"199.2\" height=\"53.7\"/>\n" +
                    "\t<rect x=\"21.6\" y=\"273.8\" class=\"st1\" width=\"199.2\" height=\"53.8\"/>\n" +
                    "\t<rect x=\"244.3\" y=\"112.5\" class=\"st1\" width=\"141.2\" height=\"215\"/>\n" +
                    "\t<rect x=\"409.1\" y=\"112.5\" class=\"st1\" width=\"199.2\" height=\"96.3\"/>\n" +
                    "\t<rect x=\"409.1\" y=\"231.3\" class=\"st1\" width=\"199.2\" height=\"96.3\"/>\n" +
                    "\t<rect x=\"633.1\" y=\"112.9\" class=\"st1\" width=\"126.9\" height=\"95.9\"/>\n" +
                    "\t<rect x=\"633.1\" y=\"231.7\" class=\"st1\" width=\"126.9\" height=\"95.9\"/>\n" +
                    "\t<rect x=\"21.6\" y=\"359.5\" class=\"st1\" width=\"199.2\" height=\"53.8\"/>\n" +
                    "\t<rect x=\"244.4\" y=\"359.5\" class=\"st1\" width=\"343.7\" height=\"53.8\"/>\n" +
                    "</g>\n" +
                    "<g id=\"文字图层\">\n" +
                    "\t<text transform=\"matrix(1 0 0 1 653.7477 62.8554)\" class=\"st2 st3\">危险化学品仓库</text>\n" +
                    "\t<text transform=\"matrix(1 0 0 1 86.2301 148.7154)\" class=\"st2 st3\">生产车间一</text>\n" +
                    "\t<text transform=\"matrix(1 0 0 1 86.2301 201.7154)\" class=\"st2 st3\">生产车间二</text>\n" +
                    "\t<text transform=\"matrix(1 0 0 1 86.2301 254.7154)\" class=\"st2 st3\">生产车间三</text>\n" +
                    "\t<text transform=\"matrix(1 0 0 1 86.2301 308.7154)\" class=\"st2 st3\">生产车间四</text>\n" +
                    "\t<text transform=\"matrix(1 0 0 1 279.8901 221.3546)\" class=\"st2 st3\">物料存放区</text>\n" +
                    "\t<text transform=\"matrix(1 0 0 1 100.2301 391.7154)\" class=\"st2 st3\">门卫室</text>\n" +
                    "\t<text transform=\"matrix(1 0 0 1 402.1901 391.715)\" class=\"st2 st3\">花坛</text>\n" +
                    "\t<text transform=\"matrix(1 0 0 1 480.6501 166.2996)\" class=\"st2 st3\">研发大楼</text>\n" +
                    "\t<text transform=\"matrix(1 0 0 1 487.6502 279.6246)\" class=\"st2 st3\">办公楼</text>\n" +
                    "\t<text transform=\"matrix(1 0 0 1 675.5451 166.2996)\" class=\"st2 st3\">停车场</text>\n" +
                    "\t<text transform=\"matrix(1 0 0 1 682.5451 284.7154)\" class=\"st2 st3\">食堂</text>\n" +
                    "\t<text transform=\"matrix(1 0 0 1 65.7729 63.0264)\" class=\"st2 st3\">配电房</text>\n" +
                    "\t<text transform=\"matrix(1 0 0 1 188.7477 63.4633)\" class=\"st2 st3\">空压机房</text>\n" +
                    "\t<text transform=\"matrix(1 0 0 1 307.7477 65.4304)\" class=\"st2 st3\">维修车间</text>\n" +
                    "\t<text transform=\"matrix(1 0 0 1 518.7477 63.7154)\" class=\"st2 st3\">成品仓库</text>\n" +
                    "</g>\n" +
                    "</svg>";

    @Override
    protected int getLayout() {
        return R.layout.activity_show_svg;
    }

    @Override
    protected void initEventAndData() {

        mTopBar.setTitle("展示SVG图片");
        mTopBar.addLeftBackImageButton()
                .setOnClickListener(v -> finish());

        RequestBuilder<PictureDrawable> requestBuilder =
                GlideApp.with(this)
                        .as(PictureDrawable.class)
                        .transition(withCrossFade())
                        .listener(new SvgSoftwareLayerSetter());
        requestBuilder
                .load(mImgRes.getBytes())
                .into(mImg);

    }

}
