package com.sdwfqin.quickseed.utils.qrbarscan;

import android.graphics.Bitmap;
import android.widget.ImageView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.Px;

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
public class CreateCodeTools {

    public static class Builder {

        private int backgroundColor = 0xffffffff;

        private int codeColor = 0xff000000;

        private int width = 800;

        /**
         * 二维码白边边距
         */
        private int margin = 16;

        private CharSequence content;

        public Builder(@NonNull CharSequence text) {
            this.content = text;
        }

        public Builder backgroundColor(@ColorInt int backgroundColor) {
            this.backgroundColor = backgroundColor;
            return this;
        }

        public Builder codeColor(@ColorInt int codeColor) {
            this.codeColor = codeColor;
            return this;
        }

        public Builder setWidth(@Px int width) {
            this.width = width;
            return this;
        }

        public Builder setMargin(@Px int margin) {
            this.margin = margin;
            return this;
        }

        public Bitmap builder() {
            return builder(null);
        }

        public Bitmap builder(@Nullable ImageView imageView) {
            Bitmap bitmap = CreateCodeTools.creatQRCode(content, width, backgroundColor, codeColor, margin);
            if (imageView != null) {
                imageView.setImageBitmap(bitmap);
            }
            return bitmap;
        }
    }

    //------------------------------以下为生成二维码算法------------------------------

    public static Bitmap creatQRCode(CharSequence content, int QR_WIDTH, int backgroundColor, int codeColor, int margin) {
        Bitmap bitmap = null;
        try {
            // 判断URL合法性
            if (content == null || "".equals(content) || content.length() < 1) {
                return null;
            }
            Hashtable<EncodeHintType, Object> hints = new Hashtable<>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            hints.put(EncodeHintType.MARGIN, margin);
            // 图像数据转换，使用了矩阵转换
            BitMatrix bitMatrix = new QRCodeWriter().encode(content + "", BarcodeFormat.QR_CODE, QR_WIDTH, QR_WIDTH, hints);
            int[] pixels = new int[QR_WIDTH * QR_WIDTH];
            // 下面这里按照二维码的算法，逐个生成二维码的图片，
            // 两个for循环是图片横列扫描的结果
            for (int y = 0; y < QR_WIDTH; y++) {
                for (int x = 0; x < QR_WIDTH; x++) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * QR_WIDTH + x] = codeColor;
                    } else {
                        pixels[y * QR_WIDTH + x] = backgroundColor;
                    }
                }
            }
            // 生成二维码图片的格式，使用ARGB_8888
            bitmap = Bitmap.createBitmap(QR_WIDTH, QR_WIDTH, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, QR_WIDTH, 0, 0, QR_WIDTH, QR_WIDTH);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static Bitmap creatQRCode(CharSequence content, int QR_WIDTH, int margin) {
        return creatQRCode(content, QR_WIDTH, 0xffffffff, 0xff000000, margin);
    }

    public static Bitmap creatQRCode(CharSequence content, int QR_WIDTH) {
        return creatQRCode(content, 800, 16);
    }

    public static Bitmap creatQRCode(CharSequence content) {
        return creatQRCode(content, 16);
    }

    //==============================================================================================二维码算法结束


    /**
     * @param content   需要转换的字符串
     * @param QR_WIDTH  二维码的宽度
     * @param QR_HEIGHT 二维码的高度
     * @param iv_code   图片空间
     */
    public static void createQRCode(String content, int QR_WIDTH, int QR_HEIGHT, ImageView iv_code) {
        iv_code.setImageBitmap(creatQRCode(content, QR_WIDTH, QR_HEIGHT));
    }

    /**
     * QR_WIDTH  二维码的宽度
     * QR_HEIGHT 二维码的高度
     *
     * @param content 需要转换的字符串
     * @param iv_code 图片空间
     */
    public static void createQRCode(String content, ImageView iv_code) {
        iv_code.setImageBitmap(creatQRCode(content));
    }
}
