package com.sdwfqin.qrscan;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.Hashtable;

/**
 * 描述：创建二维码
 *
 * @author 张钦
 * @date 2018/1/25
 */
public class QrCreateTool {

    /**
     * 获取建造者
     *
     * @param text 样式字符串文本
     * @return {@link QrCreateTool.Builder}
     */
    public static QrCreateTool.Builder builder(@NonNull CharSequence text) {
        return new QrCreateTool.Builder(text);
    }

    public static class Builder {

        private int backgroundColor = 0xFFFFFFFF;

        private int codeColor = 0xFF000000;

        private int codeSide = 800;

        private CharSequence content;

        public Builder backColor(int backgroundColor) {
            this.backgroundColor = backgroundColor;
            return this;
        }

        public Builder codeColor(int codeColor) {
            this.codeColor = codeColor;
            return this;
        }

        public Builder codeSide(int codeSide) {
            this.codeSide = codeSide;
            return this;
        }

        public Builder(@NonNull CharSequence text) {
            this.content = text;
        }

        public Bitmap into(ImageView imageView) {
            Bitmap bitmap = QrCreateTool.creatQRCode(content, codeSide, codeSide, backgroundColor, codeColor);
            if (imageView != null) {
                imageView.setImageBitmap(bitmap);
            }
            return bitmap;
        }
    }

    /**
     * 生成二维码算法
     *
     * @param content         文本内容
     * @param qrWidth         宽
     * @param qrHeight        高
     * @param backgroundColor 背景色
     * @param codeColor       前景色
     * @return
     */
    public static Bitmap creatQRCode(CharSequence content, int qrWidth, int qrHeight, int backgroundColor, int codeColor) {
        Bitmap bitmap = null;
        try {
            // 判断URL合法性
            if (content == null || "".equals(content) || content.length() < 1) {
                return null;
            }

            Hashtable<EncodeHintType, Object> hints = new Hashtable<>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            hints.put(EncodeHintType.MARGIN, 1);
            // 图像数据转换，使用了矩阵转换
            BitMatrix bitMatrix = new QRCodeWriter()
                    .encode(content + "", BarcodeFormat.QR_CODE, qrWidth, qrHeight, hints);
            int[] pixels = new int[qrWidth * qrHeight];
            // 下面这里按照二维码的算法，逐个生成二维码的图片，
            // 两个for循环是图片横列扫描的结果
            for (int y = 0; y < qrHeight; y++) {
                for (int x = 0; x < qrWidth; x++) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * qrWidth + x] = codeColor;
                    } else {
                        pixels[y * qrWidth + x] = backgroundColor;
                    }
                }
            }
            // 生成二维码图片的格式，使用ARGB_8888
            bitmap = Bitmap.createBitmap(qrWidth, qrHeight, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, qrWidth, 0, 0, qrWidth, qrHeight);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    /**
     * @param content  文本
     * @param qrWidth  宽
     * @param qrHeight 高
     * @return 二维码图片
     */
    public static Bitmap creatQRCode(CharSequence content, int qrWidth, int qrHeight) {
        return creatQRCode(content, qrWidth, qrHeight, 0xFFFFFFFF, 0xFF000000);
    }

    /**
     * @param content 文本
     * @return 二维码图片
     */
    public static Bitmap creatQRCode(CharSequence content) {
        return creatQRCode(content, 800, 800);
    }

    /**
     * @param content  需要转换的字符串
     * @param qrWidth  二维码的宽度
     * @param qrHeight 二维码的高度
     * @param imgCode  图片空间
     */
    public static void createQRCode(String content, int qrWidth, int qrHeight, ImageView imgCode) {
        imgCode.setImageBitmap(creatQRCode(content, qrWidth, qrHeight));
    }

    /**
     * @param content 需要转换的字符串
     * @param imgCode 图片空间
     */
    public static void createQRCode(String content, ImageView imgCode) {
        imgCode.setImageBitmap(creatQRCode(content));
    }
}
