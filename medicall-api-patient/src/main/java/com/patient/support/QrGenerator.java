package com.patient.support;

import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

@Component
public class QrGenerator {

    @Value("${medicall.patient.qr.base-url}")
    private String baseUrl;

    private static final int DEFAULT_WIDTH = 300;
    private static final int DEFAULT_HEIGHT = 300;

    public String generateQrCodeImage(String qrToken){
        String qrUrl = baseUrl + "/" + qrToken;

        try{
            // QR 코드 생성 옵션 설정
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            hints.put(EncodeHintType.MARGIN, 1);
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);

            // QR 코드 매트릭스 생성
            QRCodeWriter qrCodeWriter = new QRCodeWriter();

            BitMatrix bitMatrix = qrCodeWriter.encode(
                    qrUrl,
                    BarcodeFormat.QR_CODE,
                    DEFAULT_WIDTH,
                    DEFAULT_HEIGHT,
                    hints
            );

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", baos);
            byte[] bytes = baos.toByteArray();

            return Base64.getEncoder().encodeToString(bytes);

        }catch(Exception e){
            throw new QrCodeGeneratorException("QR code 생성 실패: " + e.getMessage(), e);
        }
    }

}
