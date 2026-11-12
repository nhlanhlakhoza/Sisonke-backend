package com.example.Sisonke.backend.Repository;

import com.example.Sisonke.backend.models.ledger.entity.LedgerAccount;
import com.example.Sisonke.backend.models.ledger.enums.LedgerAccountType;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface LedgerAccountRepository extends JpaRepository<LedgerAccount, UUID> {

    Optional<LedgerAccount> findByAccountTypeAndOwner_Id(
        LedgerAccountType accountType,
        UUID userId
    );

    Optional<LedgerAccount> findByCampaignId(UUID campaignId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
        SELECT a
        FROM LedgerAccount a
        WHERE a.id = :accountId
    """)
    Optional<LedgerAccount> findByIdForUpdate(
        @Param("accountId") UUID accountId
    );
}
