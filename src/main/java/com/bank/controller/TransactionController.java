package com.bank.controller;

import com.bank.api.model.CreateTransactionRequest;
import com.bank.api.model.Transaction;
import com.bank.controller.utils.TransactionUtil;
import com.bank.domain.model.CreateTransactionParams;
import com.bank.domain.service.TransactionService;
import com.bank.exception.TransactionNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;


@RestController
@RequestMapping("/transactions")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;
    @PostMapping("")
    public Transaction create(@RequestBody CreateTransactionRequest request) {
        var params = CreateTransactionParams.builder().
                type(TransactionUtil.toDomainType(request.getType())).
                amount(new BigDecimal(request.getAmount())).
                fromAccountId(request.getFromAccountId()).
                toAccountId(request.getToAccountId()).
                build();
        return TransactionUtil.fromDomain(transactionService.create(params));
    }

    @PostMapping("/{id}/submit")
    public Transaction submit(@PathVariable long id) {
        return TransactionUtil.fromDomain(transactionService.submit(id));
    }

    @GetMapping("/{id}")
    public Transaction getById(@PathVariable long id) {
        var optionalTransaction = transactionService.getById(id);
        if (optionalTransaction.isEmpty()) {
            throw new TransactionNotFoundException(id);
        }
        return TransactionUtil.fromDomain(optionalTransaction.get());
    }
}
