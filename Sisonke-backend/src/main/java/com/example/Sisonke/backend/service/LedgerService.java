package com.example.Sisonke.backend.service;

import com.example.Sisonke.backend.Repository.LedgerEntryRepository;
import com.example.Sisonke.backend.ledger.entity.LedgerAccount;
import com.example.Sisonke.backend.ledger.entity.LedgerEntry;
import com.example.Sisonke.backend.wallet.entity.WalletTransaction;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class LedgerService {

    private final LedgerEntryRepository ledgerEntryRepository;

    @Transactional
    public void transfer(
        WalletTransaction transaction,
        LedgerAccount debitAccount,
        LedgerAccount creditAccount,
        BigDecimal amount
    ) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero.");
        }

        if (debitAccount.getId().equals(creditAccount.getId())) {
            throw new IllegalArgumentException(
                "Debit and credit accounts cannot be the same."
            );
        }

        LedgerEntry debit = new LedgerEntry();
        debit.setTransaction(transaction);
        debit.setAccount(debitAccount);
        debit.setDebit(amount);
        debit.setCredit(BigDecimal.ZERO);

        LedgerEntry credit = new LedgerEntry();
        credit.setTransaction(transaction);
        credit.setAccount(creditAccount);
        credit.setDebit(BigDecimal.ZERO);
        credit.setCredit(amount);

        ledgerEntryRepository.save(debit);
        ledgerEntryRepository.save(credit);
    }

    @Transactional(readOnly = true)
    public BigDecimal getBalance(LedgerAccount account) {
        return ledgerEntryRepository.calculateBalance(account.getId());
    }
}
