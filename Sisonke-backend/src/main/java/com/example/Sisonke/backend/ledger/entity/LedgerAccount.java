package com.example.Sisonke.backend.ledger.entity;

import com.example.Sisonke.backend.ledger.enums.LedgerAccountType;
import com.example.Sisonke.backend.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "ledger_accounts")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LedgerAccount {
    @Id
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(length = 36, updatable = false)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(name = "account_type", nullable = false, length = 30)
    private LedgerAccountType accountType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_user_id")
    private User owner;

    // Campaign ID is associated with campaign fund accounts.
    // It is a scalar here to avoid a circular entity relationship.
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(name = "campaign_id", length = 36)
    private UUID campaignId;

    @Column(nullable = false, length = 3)
    @Builder.Default
    private String currency = "ZAR";

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    void onCreate() {
        if (id == null) id = UUID.randomUUID();
        createdAt = LocalDateTime.now();
    }

}
