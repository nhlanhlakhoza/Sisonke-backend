package com.example.Sisonke.backend.controller;

import com.example.Sisonke.backend.dto.request.CampaignRequest;
import com.example.Sisonke.backend.dto.response.CampaignResponse;
import com.example.Sisonke.backend.models.user.entity.User;
import com.example.Sisonke.backend.service.CampaignService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/campaigns")
@RequiredArgsConstructor
public class CampaignController {

    private final CampaignService campaignService;

    @PostMapping
    public ResponseEntity<CampaignResponse> createCampaign(
        @Valid @RequestBody CampaignRequest request,
        Authentication authentication
    ) {

        User creator = (User) authentication.getPrincipal();

        CampaignResponse response =
            campaignService.createCampaign(creator, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}

