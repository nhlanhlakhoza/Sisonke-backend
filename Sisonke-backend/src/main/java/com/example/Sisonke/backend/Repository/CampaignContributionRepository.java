package com.example.Sisonke.backend.Repository;

import com.example.Sisonke.backend.models.contribution.entity.CampaignContribution;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CampaignContributionRepository extends JpaRepository<CampaignContribution, UUID> {

}
