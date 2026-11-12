package com.example.Sisonke.backend.Repository;

import com.example.Sisonke.backend.payment.entity.Deposit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface DepositRepository extends JpaRepository<Deposit, UUID> {

    Optional<Deposit> findByOzowReference(String ozowReference);

    Optional<Deposit> findByWalletTransaction_Id(UUID transactionId);
}
