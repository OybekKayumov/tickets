package com.ok.tickets.services;

import com.ok.tickets.domain.enteties.QrCode;
import com.ok.tickets.domain.enteties.Ticket;

public interface QrCodeService {

	QrCode generateQrCode(Ticket ticket);
}
