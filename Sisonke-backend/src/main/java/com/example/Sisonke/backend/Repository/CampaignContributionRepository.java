package com.example.Sisonke.backend.Repository;

import com.example.Sisonke.backend.contribution.entity.CampaignContribution;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CampaignContributionRepository extends JpaRepository<CampaignContribution, UUID> {

    List<CampaignContribution> findByCampaign_IdOrderByCreatedAtDesc(
        UUID campaignId
    );

    List<CampaignContribution> findByContributor_IdOrderByCreatedAtDesc(
        UUID contributorId
    );

    boolean existsByWalletTransaction_Id(UUID transactionId);
    Optional<CampaignContribution> findByWalletTransaction_Id(UUID transactionId);
}
