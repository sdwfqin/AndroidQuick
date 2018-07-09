package com.sdwfqin.qrscan;

import android.graphics.Bitmap;

import com.blankj.utilcode.util.ImageUtils;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;

import java.util.Hashtable;
import java.util.Vector;

/**
 * 描述：解析二维码
 *
 * @author 张钦
 * @date 2018/1/25
 */
public class QrBarTool {

    /**
     * 解析图片中的 二维码 或者 条形码
     *
     * @param photo 待解析的图片
     * @return Result 解析结果，解析识别时返回NULL
     */
    public static Result decodeFromPhoto(Bitmap photo) {
        Result rawResult = null;
        if (photo != null) {
            // 为防止原始图片过大导致内存溢出，这里先缩小原图显示，然后释放原始Bitmap占用的内存
            Bitmap smallBitmap = ImageUtils.scale(photo, photo.getWidth() / 2, photo.getHeight() / 2);
            // 释放原始图片占用的内存，防止out of memory异常发生
            photo.recycle();

            MultiFormatReader multiFormatReader = new MultiFormatReader();

            // 解码的参数
            Hashtable<DecodeHintType, Object> hints = new Hashtable<>(2);
            // 可以解析的编码类型
            Vector<BarcodeFormat> decodeFormats = new Vector<>();

            decodeFormats.add(BarcodeFormat.UPC_A);
            decodeFormats.add(BarcodeFormat.UPC_E);
            decodeFormats.add(BarcodeFormat.UPC_EAN_EXTENSION);
            decodeFormats.add(BarcodeFormat.CODABAR);
            decodeFormats.add(BarcodeFormat.RSS_14);
            decodeFormats.add(BarcodeFormat.RSS_EXPANDED);
            // 商品码
            decodeFormats.add(BarcodeFormat.EAN_13);
            decodeFormats.add(BarcodeFormat.EAN_8);
            decodeFormats.add(BarcodeFormat.CODE_39);
            decodeFormats.add(BarcodeFormat.CODE_93);
            // CODE128码是广泛应用在企业内部管理、生产流程、物流控制系统方面的条码码制
            decodeFormats.add(BarcodeFormat.CODE_128);
            // 主要用于运输包装，是印刷条件较差，不允许印刷EAN-13和UPC-A条码时应选用的一种条码。
            decodeFormats.add(BarcodeFormat.ITF);
            // 矩阵二维码
            decodeFormats.add(BarcodeFormat.QR_CODE);
            // 二维码（防伪、统筹等）
            decodeFormats.add(BarcodeFormat.DATA_MATRIX);

            decodeFormats.add(BarcodeFormat.AZTEC);
            decodeFormats.add(BarcodeFormat.MAXICODE);
            decodeFormats.add(BarcodeFormat.PDF_417);
            hints.put(DecodeHintType.POSSIBLE_FORMATS, decodeFormats);
            // 设置继续的字符编码格式为UTF8
            // hints.put(DecodeHintType.CHARACTER_SET, "UTF8");
            // 设置解析配置参数
            multiFormatReader.setHints(hints);

            // 开始对图像资源解码
            try {
                rawResult = multiFormatReader.decodeWithState(new BinaryBitmap(new HybridBinarizer(new BitmapLuminanceSource(smallBitmap))));
            } catch (Exception e) {
                e.printStackTrace();
//                LogUtils.e(e);
            }
        }
        return rawResult;
    }
}
