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


}
