package com.samsung.cameraxandzxing;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;

import java.nio.ByteBuffer;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;

public class QRAnalyzer implements ImageAnalysis.Analyzer {

    public static final String QR = "QR";
    private final MultiFormatReader multiFormatReader;

    public QRAnalyzer() {
        multiFormatReader = new MultiFormatReader();
        Map<DecodeHintType, Object> hintTypeObjectMap = new EnumMap<>(DecodeHintType.class);
        hintTypeObjectMap.put(DecodeHintType.POSSIBLE_FORMATS, EnumSet.of(BarcodeFormat.QR_CODE));
        multiFormatReader.setHints(hintTypeObjectMap);
    }

    @Override
    public void analyze(@NonNull ImageProxy image) {

        ByteBuffer byteBuffer = image.getPlanes()[0].getBuffer();

        byte[] data = new byte[byteBuffer.remaining()];
        byteBuffer.get(data);

        int[] intData = new int[data.length];
        for (int i = 0; i < intData.length; i++) {
            intData[i] = data[i] & 0xFF;
        }

        int height = image.getHeight();
        int width = image.getWidth();

        try {
            Result result = multiFormatReader.decodeWithState(
                    new BinaryBitmap(
                            new HybridBinarizer(
                                    new RGBLuminanceSource(width, height, intData)
                            )
                    )
            );
            Log.i(QR, result.toString());
        } catch (NotFoundException e) {
            //e.printStackTrace();
        }finally {
            multiFormatReader.reset();
            image.close();;
        }


    }
}
