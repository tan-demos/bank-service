package com.bank.domain.repository;

import com.bank.domain.model.Page;

public interface PagingRepository<T> {
    Page<T> getPage(int page, int size);
}
