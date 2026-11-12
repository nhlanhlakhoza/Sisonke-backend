package com.example.Sisonke.backend.models.payment.entity;

import com.example.Sisonke.backend.models.payment.enums.NotificationStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.UUID;
@Entity
@Table(
    name = "payment_notifications",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_payment_notification",
            columnNames = {
                "provider",
                "provider_reference",
                "payload_hash"
            }
        )
    }
)

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentNotification {
    @Id
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(length = 36, updatable = false)
    private UUID id;

    @Column(nullable = false, length = 30)
    @Builder.Default
    private String provider = "OZOW";

    @Column(name = "provider_reference",
        nullable = false, length = 150)
    private String providerReference;

    @Column(name = "payload_hash",
        nullable = false, length = 128)
    private String payloadHash;

    @Enumerated(EnumType.STRING)
    @Column(name = "processing_status",
        nullable = false, length = 20)
    @Builder.Default
    private NotificationStatus processingStatus =
        NotificationStatus.RECEIVED;

    @Column(name = "received_at", nullable = false, updatable = false)
    private LocalDateTime receivedAt;

    @Column(name = "processed_at")
    private LocalDateTime processedAt;

    // Store the verified/sanitised payload for audit purposes.
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(nullable = false)
    private String payload;

    @PrePersist
    void onCreate() {
        if (id == null) id = UUID.randomUUID();
        receivedAt = LocalDateTime.now();
    }
}
