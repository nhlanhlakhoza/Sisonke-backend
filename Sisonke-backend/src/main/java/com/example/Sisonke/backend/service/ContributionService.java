
package com.example.Sisonke.backend.service;

import com.example.Sisonke.backend.Repository.CampaignRepository;
import com.example.Sisonke.backend.Repository.CampaignContributionRepository;
import com.example.Sisonke.backend.Repository.UserRepository;
import com.example.Sisonke.backend.models.campaign.entity.Campaign;
import com.example.Sisonke.backend.models.campaign.enums.CampaignStatus;
import com.example.Sisonke.backend.models.contribution.entity.CampaignContribution;
import com.example.Sisonke.backend.models.contribution.enums.ContributionStatus;
import com.example.Sisonke.backend.models.user.entity.User;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ContributionService {

    private final CampaignRepository campaignRepository;
    private final CampaignContributionRepository contributionRepository;
    private final UserRepository userRepository;

    @Transactional
    public CampaignContribution contribute(
        UUID userId,
        UUID campaignId,
        BigDecimal amount
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

        User contributor = userRepository.findById(userId)
            .orElseThrow(() ->
                new IllegalArgumentException("Member not found.")
            );

        Campaign campaign = campaignRepository.findById(campaignId)
            .orElseThrow(() ->
                new IllegalArgumentException("Campaign not found.")
            );

        if (campaign.getStatus() != CampaignStatus.ACTIVE) {
            throw new IllegalArgumentException(
                "This campaign is not accepting contributions."
            );
        }

        CampaignContribution contribution = new CampaignContribution();

        contribution.setCampaign(campaign);
        contribution.setContributor(contributor);
        contribution.setAmount(amount);
        contribution.setCurrency("ZAR");
        contribution.setStatus(ContributionStatus.PENDING);

        return contributionRepository.save(contribution);
    }
}
