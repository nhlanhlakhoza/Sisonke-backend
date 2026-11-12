package com.example.Sisonke.backend.Repository;

import com.example.Sisonke.backend.models.withdrawal.entity.WalletWithdrawal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface WalletWithdrawalRepository extends JpaRepository<WalletWithdrawal, UUID> {

    Optional<WalletWithdrawal> findByOzowReference(String ozowReference);

    Optional<WalletWithdrawal> findByWalletTransaction_Id(UUID transactionId);
}
