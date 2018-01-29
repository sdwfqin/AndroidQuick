package com.sdwfqin.quickseed.base;

import com.blankj.utilcode.util.SDCardUtils;

/**
 * 描述：全局配置
 *
 * @author  张钦
 * @date 2017/9/25
 */
public class Constants {

    // 活动请求码
    public static final int RESULT_CODE_1 = 101;
    public static final int RESULT_CODE_2 = 102;
    public static final int RESULT_CODE_3 = 103;
    public static final int RESULT_CODE_4 = 104;
    public static final int RESULT_CODE_5 = 105;

    /**
     * 刷新我的页面
     */
    public static final int EVENT_1 = 105;
    /**
     * 刷新选择银行卡页面
     */
    public static final int EVENT_2 = 106;

    /**
     * 状态栏高度
     */
    public static int STATUS_HEIGHT = 25;

    /**
     * 上传文件表单
     */
    public static final String MULTIPART_FORM_DATA = "multipart/form-data";

    /**
     * 是否打印日志，正式版需要设置为false
     */
    public static final boolean LOG_TYPE = true;

    /**
     * 图片保存位置
     */
    public static final String SAVE_REAL_PATH = SDCardUtils.getSDCardPaths().get(0) + "/FlShop";

    /**
     * 文件共享
     */
    public static final String FILE_PROVIDER = "com.sdwfqin.quickseed.fileprovider";

    /**
     * BaseUrl
     */
    public static final String BASE_URL = "http://app.dedehr.com/";


    public static final String HEAD = "<!DOCTYPE html>\n" +
            "<html lang=\"en\">\n" +
            "    <head>\n" +
            "        <meta charset=\"UTF-8\">\n" +
            "        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, user-scalable=0, minimum-scale=1.0, maximum-scale=1.0\">\n" +
            "        <meta name=\"apple-mobile-web-app-capable\" content=\"yes\">\n" +
            "        <meta name=\"apple-mobile-web-app-status-bar-style\" content=\"black\">\n" +
            "        <meta content=\"telephone=no\" name=\"format-detection\">\n" +
            "        <title>商品详情</title>\n" +
            "        <style>\n" +
            "           body{ margin:0; border:0}\n" +
            "       </style>\n" +
            "    </head>\n" +
            "    <body>";

    public static final String END = "</body>\n" +
            "</html>";
}
