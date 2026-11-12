package com.example.Sisonke.backend.service;

import com.example.Sisonke.backend.Repository.*;
import com.example.Sisonke.backend.models.campaign.entity.Campaign;
import com.example.Sisonke.backend.models.contribution.entity.CampaignContribution;
import com.example.Sisonke.backend.models.ledger.entity.LedgerAccount;
import com.example.Sisonke.backend.models.wallet.entity.Wallet;
import com.example.Sisonke.backend.models.wallet.entity.WalletTransaction;
import com.example.Sisonke.backend.models.wallet.enums.TransactionStatus;
import com.example.Sisonke.backend.models.wallet.enums.TransactionType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ContributionService {

    private final WalletRepository walletRepository;
    private final CampaignRepository campaignRepository;
    private final LedgerAccountRepository ledgerAccountRepository;
    private final WalletTransactionRepository transactionRepository;
    private final CampaignContributionRepository contributionRepository;
    private final LedgerService ledgerService;

    @Transactional
    public CampaignContribution contribute(
        UUID userId,
        UUID campaignId,
        BigDecimal amount,
        String idempotencyKey
    ) {

        if (amount == null ||
            amount.compareTo(new BigDecimal("10.00")) < 0) {
            throw new IllegalArgumentException(
                "The minimum contribution is R10.00."
            );
        }

        if (amount.scale() > 2) {
            throw new IllegalArgumentException(
                "Contribution amount cannot have more than two decimal places."
            );
        }

        // Return the existing transaction if this request was already processed.
        var existing = transactionRepository
            .findByIdempotencyKey(idempotencyKey);

        if (existing.isPresent()) {
            return contributionRepository
                .findByWalletTransaction_Id(existing.get().getId())
                .orElseThrow(() -> new IllegalStateException(
                    "Existing transaction has no contribution record."
                ));
        }

        // Lock the member's wallet.
        Wallet wallet = walletRepository.findByUserIdForUpdate(userId)
            .orElseThrow(() ->
                new IllegalArgumentException("Wallet not found.")
            );

        // Lock the campaign to coordinate contributions and campaign withdrawals.
        Campaign campaign = campaignRepository.findByIdForUpdate(campaignId)
            .orElseThrow(() ->
                new IllegalArgumentException("Campaign not found.")
            );

        LedgerAccount walletAccount = ledgerAccountRepository
            .findByIdForUpdate(wallet.getLedgerAccount().getId())
            .orElseThrow(() ->
                new IllegalStateException("Wallet ledger account not found.")
            );

        LedgerAccount campaignAccount = ledgerAccountRepository
            .findByCampaignId(campaign.getId())
            .orElseThrow(() ->
                new IllegalStateException("Campaign fund account not found.")
            );

        // Check available wallet funds.
        BigDecimal balance = ledgerService.getBalance(walletAccount);

        if (balance.compareTo(amount) < 0) {
            throw new IllegalArgumentException(
                "Insufficient wallet funds."
            );
        }

        WalletTransaction transaction = new WalletTransaction();
        transaction.setWallet(wallet);
        transaction.setTransactionType(TransactionType.CONTRIBUTION);
        transaction.setAmount(amount);
        transaction.setCurrency("ZAR");
        transaction.setStatus(TransactionStatus.COMPLETED);
        transaction.setReference(UUID.randomUUID().toString());
        transaction.setIdempotencyKey(idempotencyKey);
        transaction.setDescription("Campaign contribution");

        transaction = transactionRepository.save(transaction);

        ledgerService.transfer(
            transaction,
            walletAccount,
            campaignAccount,
            amount
        );

        CampaignContribution contribution = new CampaignContribution();
        contribution.setCampaign(campaign);
        contribution.setContributor(wallet.getUser());
        contribution.setWalletTransaction(transaction);
        contribution.setAmount(amount);
        contribution.setCurrency("ZAR");

        return contributionRepository.save(contribution);
    }
}
