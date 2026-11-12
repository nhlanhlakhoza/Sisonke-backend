package com.example.Sisonke.backend.Repository;

import com.example.Sisonke.backend.models.campaign.entity.Campaign;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CampaignRepository extends JpaRepository<Campaign, UUID> {

    List<Campaign> findByCreator_Id(UUID creatorId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
        SELECT c
        FROM Campaign c
        WHERE c.id = :campaignId
    """)
    Optional<Campaign> findByIdForUpdate(
        @Param("campaignId") UUID campaignId
    );

    Optional<Campaign> findByIdAndCreator_Id(
        UUID campaignId,
        UUID creatorId
    );
}
