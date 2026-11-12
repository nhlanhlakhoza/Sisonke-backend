package com.example.Sisonke.backend.Repository;

import com.example.Sisonke.backend.models.wallet.entity.WalletTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface WalletTransactionRepository extends JpaRepository<WalletTransaction, UUID> {

    Optional<WalletTransaction> findByIdempotencyKey(String idempotencyKey);

    Optional<WalletTransaction> findByReference(String reference);

    boolean existsByIdempotencyKey(String idempotencyKey);
}
