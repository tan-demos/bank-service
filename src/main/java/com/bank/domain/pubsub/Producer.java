package com.bank.domain.pubsub;

import com.bank.domain.model.Transaction;

public interface Producer {
    void send(String eventName, Transaction transaction);
}
