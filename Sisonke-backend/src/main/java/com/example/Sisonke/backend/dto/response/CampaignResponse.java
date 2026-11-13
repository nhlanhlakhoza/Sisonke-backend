package com.example.Sisonke.backend.dto.response;

import com.example.Sisonke.backend.models.campaign.enums.CampaignStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public class CampaignResponse {

    private UUID id;
    private String title;
    private String description;
    private String category;
    private CampaignStatus status;
    private String creatorName;
    private LocalDateTime createdAt;

    public CampaignResponse(
        UUID id,
        String title,
        String description,
        String category,
        CampaignStatus status,
        String creatorName,
        LocalDateTime createdAt
    ) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.category = category;
        this.status = status;
        this.creatorName = creatorName;
        this.createdAt = createdAt;
    }

    public UUID getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public CampaignStatus getStatus() {
        return status;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
