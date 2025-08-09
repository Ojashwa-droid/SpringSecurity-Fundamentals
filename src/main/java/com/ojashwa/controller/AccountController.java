package com.ojashwa.controller;

import com.ojashwa.model.Accounts;
import com.ojashwa.repository.AccountsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AccountController {
    private final AccountsRepository accountsRepository;

    /**
     * This is a simple controller that handles one url and returns a simple String
     */

    @GetMapping("/myAccount")
    public Accounts getAccountDetails (@RequestParam Long id) {
        Accounts accounts = accountsRepository.findByCustomerId(id);
        if (accounts != null) {
            return accounts;
        } else {
            return null;
        }

    }
}


