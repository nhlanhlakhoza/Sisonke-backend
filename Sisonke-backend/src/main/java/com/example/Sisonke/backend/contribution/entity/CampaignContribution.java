package com.example.Sisonke.backend.contribution.entity;

import com.example.Sisonke.backend.campaign.entity.Campaign;
import com.example.Sisonke.backend.user.entity.User;
import com.example.Sisonke.backend.wallet.entity.WalletTransaction;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Check;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
@Entity
@Table(name = "campaign_contributions")
@Check(constraints = "amount >= 10.00")

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CampaignContribution {
    @Id
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(length = 36, updatable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "campaign_id", nullable = false)
    private Campaign campaign;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "contributor_id", nullable = false)
    private User contributor;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "wallet_transaction_id", nullable = false, unique = true)
    private WalletTransaction walletTransaction;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

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
