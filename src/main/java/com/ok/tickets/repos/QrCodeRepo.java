package com.ok.tickets.repos;

import com.ok.tickets.domain.enteties.QrCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface QrCodeRepo extends JpaRepository<QrCode, UUID> {

}
