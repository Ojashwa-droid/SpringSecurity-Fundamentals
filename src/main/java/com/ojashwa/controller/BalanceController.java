package com.ojashwa.controller;

import com.ojashwa.model.AccountTransactions;
import com.ojashwa.repository.AccountTransactionsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BalanceController {
    private final AccountTransactionsRepository accountTransactionsRepository;

    /**
     * This is a simple controller that handles one url and returns a simple String
     */

    @GetMapping("/myBalance")
    public List<AccountTransactions> getBalanceDetails(@RequestParam long id) {
        List<AccountTransactions> accountsTransactions = accountTransactionsRepository.findByCustomerIdOrderByTransactionDtDesc(id);
        if (accountsTransactions.isEmpty()) {
            return null;
        } else {
            return accountsTransactions;
        }
    }
}


