package com.example.Sisonke.backend.service;

import com.example.Sisonke.backend.Repository.CampaignRepository;
import com.example.Sisonke.backend.Repository.LedgerAccountRepository;
import com.example.Sisonke.backend.dto.request.CampaignRequest;
import com.example.Sisonke.backend.dto.response.CampaignResponse;
import com.example.Sisonke.backend.models.campaign.entity.Campaign;
import com.example.Sisonke.backend.models.campaign.enums.CampaignStatus;
import com.example.Sisonke.backend.models.ledger.entity.LedgerAccount;
import com.example.Sisonke.backend.models.ledger.enums.LedgerAccountType;
import com.example.Sisonke.backend.models.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CampaignService {

    private final CampaignRepository campaignRepository;
    private final LedgerAccountRepository ledgerAccountRepository;

    @Transactional
    public CampaignResponse createCampaign(
        User creator,
        CampaignRequest request
    ) {

        // Generate campaign UUID first
        UUID campaignId = UUID.randomUUID();

        // Create a separate fund account for this campaign
        LedgerAccount fundAccount = new LedgerAccount();

        fundAccount.setAccountType(LedgerAccountType.CAMPAIGN_FUND);
        fundAccount.setOwner(null);
        fundAccount.setCampaignId(campaignId);
        fundAccount.setCurrency("ZAR");

        // Save the campaign fund account
        LedgerAccount savedFundAccount =
            ledgerAccountRepository.save(fundAccount);

        // Create campaign
        Campaign campaign = new Campaign();

        campaign.setId(campaignId);
        campaign.setCreator(creator);
        campaign.setTitle(request.getTitle());
        campaign.setDescription(request.getDescription());
        campaign.setCategory(request.getCategory());
        campaign.setStatus(CampaignStatus.ACTIVE);
        campaign.setFundAccount(savedFundAccount);

        // Save campaign
        Campaign savedCampaign =
            campaignRepository.save(campaign);

        // Return campaign response
        return new CampaignResponse(
            savedCampaign.getId(),
            savedCampaign.getTitle(),
            savedCampaign.getDescription(),
            savedCampaign.getCategory(),
            savedCampaign.getStatus(),
            creator.getFullName(),
            savedCampaign.getCreatedAt()
        );
    }
}
