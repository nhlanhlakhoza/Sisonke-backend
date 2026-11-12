package com.example.Sisonke.backend.models.withdrawal.entity;

import com.example.Sisonke.backend.models.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.UUID;
@Entity
@Table(name = "bank_accounts")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BankAccount {

    @Id
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(length = 36, updatable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "bank_name", nullable = false, length = 100)
    private String bankName;

    @Column(name = "account_holder", nullable = false, length = 150)
    private String accountHolder;

    // Must be encrypted by the application before persistence.
    @Column(name = "account_number_encrypted",
        nullable = false, columnDefinition = "TEXT")
    private String accountNumberEncrypted;

    @Column(name = "branch_code", length = 20)
    private String branchCode;

    @Column(nullable = false)
    @Builder.Default
    private Boolean verified = false;

    @Column(nullable = false)
    @Builder.Default
    private Boolean active = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    void onCreate() {
        if (id == null) id = UUID.randomUUID();
        createdAt = LocalDateTime.now();
    }
}

