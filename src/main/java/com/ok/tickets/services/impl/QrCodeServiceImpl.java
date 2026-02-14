package com.ok.tickets.services.impl;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.ok.tickets.domain.enteties.QrCode;
import com.ok.tickets.domain.enteties.QrCodeStatusEnum;
import com.ok.tickets.domain.enteties.Ticket;
import com.ok.tickets.exceptions.QrCodeGenerationException;
import com.ok.tickets.repos.QrCodeRepo;
import com.ok.tickets.services.QrCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class QrCodeServiceImpl implements QrCodeService {

	private static final int QR_HEIGHT = 300;
	private static final int QR_WIDTH = 300;

	private final QRCodeWriter qrCodeWriter;
	private final QrCodeRepo qrCodeRepo;

	@Override
	public QrCode generateQrCode(Ticket ticket) throws QrCodeGenerationException {

		try {
			UUID uniqueId = UUID.randomUUID();
			String qrCodeImage = generateQrCodeImage(uniqueId);

			QrCode qrCode = new QrCode();
			qrCode.setId(uniqueId);
			qrCode.setStatus(QrCodeStatusEnum.ACTIVE);
			qrCode.setValue(qrCodeImage);
			qrCode.setTicket(ticket);

			return qrCodeRepo.saveAndFlush(qrCode);

		} catch (IOException | WriterException e) {
			throw new QrCodeGenerationException("Failed to generate QR Code", e);
		}
	}

	private String generateQrCodeImage(UUID uniqueId) throws WriterException , IOException{

		BitMatrix bitMatrix = qrCodeWriter.encode(
						uniqueId.toString(),
						BarcodeFormat.QR_CODE,
						QR_WIDTH,
						QR_HEIGHT
		);

		BufferedImage bufferedImage =
						MatrixToImageWriter.toBufferedImage(bitMatrix);

		try(ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

			ImageIO.write(bufferedImage, "png", baos);

			byte[] imageBytes = baos.toByteArray();

			return Base64.getEncoder().encodeToString(imageBytes);
		}
	}
}
