package com.example.Sisonke.backend.Repository;

import com.example.Sisonke.backend.models.withdrawal.entity.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BankAccountRepository extends JpaRepository<BankAccount, UUID> {

    List<BankAccount> findByUser_IdAndActiveTrue(UUID userId);

    Optional<BankAccount> findByIdAndUser_IdAndActiveTrue(
        UUID bankAccountId,
        UUID userId
    );
}
