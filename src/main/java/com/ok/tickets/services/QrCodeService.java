package com.ok.tickets.services;

import com.ok.tickets.domain.enteties.QrCode;
import com.ok.tickets.domain.enteties.Ticket;
import com.ok.tickets.exceptions.QrCodeGenerationException;

public interface QrCodeService {

	QrCode generateQrCode(Ticket ticket) throws QrCodeGenerationException;
}
