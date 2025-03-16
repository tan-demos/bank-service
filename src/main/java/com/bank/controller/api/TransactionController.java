package com.bank.controller.api;

import com.bank.api.model.CreateTransactionRequest;
import com.bank.api.model.Transaction;
import com.bank.controller.utils.TransactionUtil;
import com.bank.domain.model.CreateTransactionParams;
import com.bank.domain.service.TransactionService;
import com.bank.exception.base.InternalServerErrorException;
import com.bank.exception.base.InvalidArgumentException;
import com.bank.exception.TransactionNotFoundException;
import com.bank.util.ObjectPool;
import com.bank.util.annotation.AutoLogging;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionController {
    private final TransactionService transactionService;
    private final ObjectPool<CreateTransactionParams> createTransactionParamsPool;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
        this.createTransactionParamsPool = new ObjectPool<>(CreateTransactionParams::new);
    }

    @AutoLogging
    @PostMapping
    public Transaction create(@RequestBody CreateTransactionRequest request) throws InvalidArgumentException {
        var params = createTransactionParamsPool.borrowObject();
        params.setAmount(new BigDecimal(request.getAmount()));
        params.setType(TransactionUtil.toDomainType(request.getType()));
        params.setFromAccountId(request.getFromAccountId());
        params.setToAccountId(request.getToAccountId());
        var result = TransactionUtil.fromDomain(transactionService.create(params));
        createTransactionParamsPool.returnObject(params);
        return result;
    }

    @AutoLogging
    @PostMapping("/{id}/submit")
    public Transaction submit(@PathVariable long id) throws InvalidArgumentException, InternalServerErrorException {
        return TransactionUtil.fromDomain(transactionService.submit(id));
    }

    @AutoLogging
    @GetMapping("/{id}")
    public Transaction getById(@PathVariable long id) throws TransactionNotFoundException {
        var optionalTransaction = transactionService.getById(id);
        if (optionalTransaction.isEmpty()) {
            throw new TransactionNotFoundException(id);
        }
        return TransactionUtil.fromDomain(optionalTransaction.get());
    }
}
