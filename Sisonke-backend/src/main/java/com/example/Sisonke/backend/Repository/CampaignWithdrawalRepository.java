package com.example.Sisonke.backend.Repository;

import com.example.Sisonke.backend.withdrawal.entity.CampaignWithdrawal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CampaignWithdrawalRepository extends JpaRepository<CampaignWithdrawal, UUID> {

    List<CampaignWithdrawal> findByCampaign_IdOrderByCreatedAtDesc(
        UUID campaignId
    );

    Optional<CampaignWithdrawal> findByOzowReference(String ozowReference);
}
