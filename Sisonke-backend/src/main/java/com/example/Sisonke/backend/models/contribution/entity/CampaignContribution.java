package com.example.Sisonke.backend.models.contribution.entity;


import com.example.Sisonke.backend.models.campaign.entity.Campaign;
import com.example.Sisonke.backend.models.contribution.enums.ContributionStatus;
import com.example.Sisonke.backend.models.user.entity.User;
import jakarta.persistence.*;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
    name = "campaign_contributions",
    indexes = {
        @Index(name = "idx_contribution_campaign", columnList = "campaign_id"),
        @Index(name = "idx_contribution_user", columnList = "contributor_id")
    }
)
public class CampaignContribution {

    @Id
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(length = 36, updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "campaign_id", nullable = false)
    private Campaign campaign;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "contributor_id", nullable = false)
    private User contributor;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false, length = 3)
    private String currency = "ZAR";

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ContributionStatus status = ContributionStatus.PENDING;

    @Column(name = "payment_reference", nullable = false, unique = true, length = 100)
    private String paymentReference;

    @Column(name = "provider_reference", unique = true, length = 150)
    private String providerReference;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "paid_at")
    private LocalDateTime paidAt;

    @PrePersist
    void onCreate() {
        if (id == null) {
            id = UUID.randomUUID();
        }

        if (paymentReference == null) {
            paymentReference = "SIS-" + UUID.randomUUID();
        }

        createdAt = LocalDateTime.now();
    }

    // Getters and setters

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Campaign getCampaign() {
        return campaign;
    }

    public void setCampaign(Campaign campaign) {
        this.campaign = campaign;
    }

    public User getContributor() {
        return contributor;
    }

    public void setContributor(User contributor) {
        this.contributor = contributor;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public ContributionStatus getStatus() {
        return status;
    }

    public void setStatus(ContributionStatus status) {
        this.status = status;
    }

    public String getPaymentReference() {
        return paymentReference;
    }

    public void setPaymentReference(String paymentReference) {
        this.paymentReference = paymentReference;
    }

    public String getProviderReference() {
        return providerReference;
    }

    public void setProviderReference(String providerReference) {
        this.providerReference = providerReference;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getPaidAt() {
        return paidAt;
    }

    public void setPaidAt(LocalDateTime paidAt) {
        this.paidAt = paidAt;
    }
}
