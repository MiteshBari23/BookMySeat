package com.BookMySeat.tickets.services.impl;

import com.BookMySeat.tickets.domain.entities.QrCode;
import com.BookMySeat.tickets.domain.entities.QrCodeStatusEnum;
import com.BookMySeat.tickets.domain.entities.Ticket;
import com.BookMySeat.tickets.exceptions.QrCodeGenerationException;
import com.BookMySeat.tickets.repository.QrCodeRepository;
import com.BookMySeat.tickets.services.QrCodeService;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Base64;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class QrCodeServiceImpl implements QrCodeService {

    private static final int QrCode_HEIGHT = 300;
    private static final int QrCode_WIDTH = 300;

    private final QRCodeWriter qrCodeWriter;
    private final QrCodeRepository qrCodeRepository;

    @Override
    public QrCode generateQrCode(Ticket ticket) {
        try {
        UUID uniqueID = UUID.randomUUID();
        String qrCodeImage =   generaterQrCodeImage(uniqueID);

        QrCode qrCode = new QrCode();
        qrCode.setId(uniqueID);
        qrCode.setStatus(QrCodeStatusEnum.ACTIVE);
        qrCode.setValue(qrCodeImage);
        qrCode.setTicket(ticket);

        return qrCodeRepository.saveAndFlush(qrCode);

        } catch (IOException | WriterException e) {
            throw new QrCodeGenerationException("Failed to generate QR code", e);
        }

    }

    private String generaterQrCodeImage(UUID uniqueID) throws WriterException, IOException {
        BitMatrix bitMatrix = qrCodeWriter.encode(
                uniqueID.toString(),
                BarcodeFormat.QR_CODE,
                QrCode_HEIGHT,
                QrCode_WIDTH
        );

        BufferedImage qrCodeImage = MatrixToImageWriter.toBufferedImage(bitMatrix);

        try(ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ImageIO.write(qrCodeImage, "PNG", baos);
            byte[] imageBytes = baos.toByteArray();

            return Base64.getEncoder().encodeToString(imageBytes);
        }

    }
}
