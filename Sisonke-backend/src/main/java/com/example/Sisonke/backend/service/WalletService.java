package com.example.Sisonke.backend.service;

import com.example.Sisonke.backend.Repository.WalletRepository;
import com.example.Sisonke.backend.models.ledger.entity.LedgerAccount;
import com.example.Sisonke.backend.models.wallet.entity.Wallet;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WalletService {

    private final WalletRepository walletRepository;
    private final LedgerService ledgerService;

    @Transactional(readOnly = true)
    public BigDecimal getWalletBalance(UUID userId) {

        Wallet wallet = walletRepository.findByUser_Id(userId)
            .orElseThrow(() ->
                new IllegalArgumentException("Wallet not found.")
            );

        LedgerAccount account = wallet.getLedgerAccount();

        return ledgerService.getBalance(account);
    }
}
