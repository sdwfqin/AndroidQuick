package com.sdwfqin.quickseed.base;

import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.SDCardUtils;

/**
 * 描述：全局配置
 *
 * @author 张钦
 * @date 2017/9/25
 */
public class Constants {

    /**
     * BaseUrl
     */
    public static final String BASE_URL = "http://www.sdwfqin.com/";
    public static final String BASE_API = "api";
    public static final String BASE_SHARE = BASE_URL + "index.do?id=";

    /**
     * 支付宝
     */
    public static final String A_LI_PAY_ID = "##########";
    public static final String A_LI_PAY_URL = BASE_URL + "/alipayBalance.do";
    public static final String A_LI_PAY_RES = "##################";

    /**
     * ========================================
     * ********        状态请求码        ********
     * ========================================
     */
    public static final int RESULT_CODE_1 = 101;
    public static final int RESULT_CODE_2 = 102;
    public static final int RESULT_CODE_3 = 103;
    public static final int RESULT_CODE_4 = 104;
    public static final int RESULT_CODE_5 = 105;
    public static final int RESULT_CODE_6 = 106;
    public static final int RESULT_CODE_7 = 107;
    public static final int RESULT_CODE_8 = 108;

    /**
     * ========================================
     * ********        sp文件key        ********
     * ========================================
     */
    public static final String TEST = "######";


    /**
     * =======================================
     * ********      EventBus标识      ********
     * =======================================
     */

    /**
     * 账户余额列表
     */
    public static final int EVENT_1 = 205;

    /**
     * 充值后更新我的页面
     */
    public static final int EVENT_2 = 206;

    /**
     * =======================================
     * ********        其他配置        ********
     * =======================================
     */

    /**
     * 状态栏高度
     */
    public static int STATUS_HEIGHT = ConvertUtils.dp2px(25);

    /**
     * 图片保存位置
     */
    public static final String SAVE_REAL_PATH = SDCardUtils.getSDCardPaths().get(0) + "/sdwfqin";

    /**
     * 文件共享
     */
    public static final String FILE_PROVIDER = "com.sdwfqin.quickseed.fileprovider";

    /**
     * 上传文件表单
     */
    public static final String MULTIPART_FORM_DATA = "multipart/form-data";

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
