package com.example.Sisonke.backend.controller;

import com.example.Sisonke.backend.dto.request.ContributionRequest;
import com.example.Sisonke.backend.models.contribution.entity.CampaignContribution;
import com.example.Sisonke.backend.models.user.entity.User;
import com.example.Sisonke.backend.service.ContributionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
@RestController
@RequestMapping("/api/contributions")
@RequiredArgsConstructor
public class ContributionController {

    private final ContributionService contributionService;

    @PostMapping
    public ResponseEntity<?> createContribution(
        @Valid @RequestBody ContributionRequest request,
        Authentication authentication
    ) {

        // Get the authenticated member from JWT authentication
        User user = (User) authentication.getPrincipal();
        System.out.println("========== CONTRIBUTION REQUEST ==========");
        System.out.println("Authentication: " + authentication);
        System.out.println("Authenticated: " + authentication.isAuthenticated());
        System.out.println("Principal: " + authentication.getPrincipal());
        System.out.println("Authorities: " + authentication.getAuthorities());
        System.out.println("===========================================");
        // Create the contribution
        CampaignContribution contribution =
            contributionService.contribute(
                user.getId(),
                request.getCampaignId(),
                request.getAmount()
            );

        return ResponseEntity.status(HttpStatus.CREATED).body(
            Map.of(
                "message", "Contribution created successfully. Awaiting payment.",
                "contributionId", contribution.getId(),
                "campaignId", contribution.getCampaign().getId(),
                "contributorId", user.getId(),
                "amount", contribution.getAmount(),
                "currency", contribution.getCurrency(),
                "status", contribution.getStatus(),
                "paymentReference", contribution.getPaymentReference()
            )
        );
    }
}
