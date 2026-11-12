
package com.example.Sisonke.backend.service;

import com.example.Sisonke.backend.Repository.LedgerAccountRepository;
import com.example.Sisonke.backend.Repository.UserRepository;
import com.example.Sisonke.backend.Repository.WalletRepository;

import com.example.Sisonke.backend.models.ledger.entity.LedgerAccount;
import com.example.Sisonke.backend.models.ledger.enums.LedgerAccountType;
import com.example.Sisonke.backend.models.user.entity.User;
import com.example.Sisonke.backend.models.user.enums.UserStatus;
import com.example.Sisonke.backend.models.wallet.entity.Wallet;
import com.example.Sisonke.backend.models.wallet.enums.WalletStatus;

import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final WalletRepository walletRepository;
    private final LedgerAccountRepository ledgerAccountRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User register(
        String fullName,
        String email,
        String rawPassword
    ) {



        String normalizedEmail = email.trim().toLowerCase();


        if (userRepository.existsByEmailIgnoreCase(normalizedEmail)) {
            throw new IllegalArgumentException(
                "An account with this email already exists."
            );
        }


        User user = new User();

        user.setFullName(fullName.trim());
        user.setEmail(normalizedEmail);
        user.setPasswordHash(
            passwordEncoder.encode(rawPassword)
        );
        user.setStatus(UserStatus.ACTIVE);

        user = userRepository.save(user);


        LedgerAccount walletAccount = new LedgerAccount();

        walletAccount.setAccountType(
            LedgerAccountType.USER_WALLET
        );
        walletAccount.setOwner(user);
        walletAccount.setCurrency("ZAR");

        walletAccount = ledgerAccountRepository.save(walletAccount);


        Wallet wallet = new Wallet();

        wallet.setUser(user);
        wallet.setLedgerAccount(walletAccount);
        wallet.setCurrency("ZAR");
        wallet.setStatus(WalletStatus.ACTIVE);

        walletRepository.save(wallet);


        return user;
    }
}
