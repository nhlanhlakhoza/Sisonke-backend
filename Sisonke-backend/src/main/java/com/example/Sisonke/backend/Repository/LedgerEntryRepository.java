package com.example.Sisonke.backend.Repository;

import com.example.Sisonke.backend.models.ledger.entity.LedgerEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.UUID;

public interface LedgerEntryRepository extends JpaRepository<LedgerEntry, UUID> {

    @Query("""
        SELECT COALESCE(SUM(e.credit), 0) - COALESCE(SUM(e.debit), 0)
        FROM LedgerEntry e
        WHERE e.account.id = :accountId
    """)
    BigDecimal calculateBalance(@Param("accountId") UUID accountId);
}
