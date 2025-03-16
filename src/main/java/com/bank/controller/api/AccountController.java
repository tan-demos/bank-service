package com.bank.controller.api;

import com.bank.api.model.Account;
import com.bank.api.model.AddAccountRequest;
import com.bank.controller.utils.AccountUtil;
import com.bank.domain.service.AccountService;
import com.bank.exception.AccountNotFoundException;
import com.bank.util.annotation.AutoLogging;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/accounts")
public class AccountController {
    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @AutoLogging
    @PostMapping
    public Account add(@RequestBody AddAccountRequest request) {
        var balance = request.getBalance() != null ? new BigDecimal(request.getBalance()) : BigDecimal.ZERO;
        return AccountUtil.fromDomain(accountService.add(request.getAccountId(), balance));
    }

    @AutoLogging
    @GetMapping("/{id}")
    public Account get(@PathVariable long id) {
        var optionalAccount = accountService.getById(id);
        if (optionalAccount.isEmpty()) {
            throw new AccountNotFoundException(id);
        }
        return AccountUtil.fromDomain(optionalAccount.get());
    }

    @AutoLogging
    @GetMapping("")
    public List<Account> batchGet(@RequestParam("ids") List<Long> ids) {
        return accountService.batchGet(ids).stream().map(AccountUtil::fromDomain).collect(Collectors.toList());
    }
}
