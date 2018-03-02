package com.sdwfqin.quicklib.base;

/**
 * 描述：全局配置
 *
 * @author 张钦
 * @date 2017/9/25
 */
public class Constants {

    /**
     * 上传文件表单
     */
    public static final String MULTIPART_FORM_DATA = "multipart/form-data";

    /**
     * 图片保存位置
     */
    public static String SAVE_REAL_PATH = null;

    /**
     * BaseUrl
     */
    public static String BASE_URL = null;

    /**
     * 是否打印日志，正式版需要设置为false
     */
    public static boolean LOG_TYPE = true;


    public static String APP_ID = "123456789";


    public static final String HEAD = "<!DOCTYPE html>\n" +
            "<html lang=\"en\">\n" +
            "    <head>\n" +
            "        <meta charset=\"UTF-8\">\n" +
            "        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, user-scalable=no, minimum-scale=1.0, maximum-scale=1.0\">\n" +
            "        <meta name=\"apple-mobile-web-app-capable\" content=\"yes\">\n" +
            "        <meta name=\"apple-mobile-web-app-status-bar-style\" content=\"black\">\n" +
            "        <meta content=\"telephone=no\" name=\"format-detection\">\n" +
            "        <title>商品详情</title>\n" +
            "    <style>\n" +
            "       body{ margin:0; border:0}\n" +
            "       img{width:98%;\n" +
            "       \tmargin-left:1%;\n" +
            "       \tmargin-right:1%;\n" +
            "       }\n" +
            "   </style>" +
            "    </head>\n" +
            "    <body>";

    public static final String END = "</body>\n" +
            "</html>";
}
