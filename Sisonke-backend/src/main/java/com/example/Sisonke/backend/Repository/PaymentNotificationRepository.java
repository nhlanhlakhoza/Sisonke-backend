package com.example.Sisonke.backend.Repository;

import com.example.Sisonke.backend.payment.entity.PaymentNotification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PaymentNotificationRepository  extends JpaRepository<PaymentNotification, UUID> {
    boolean existsByProviderAndProviderReferenceAndPayloadHash(
        String provider,
        String providerReference,
        String payloadHash
    );
}
