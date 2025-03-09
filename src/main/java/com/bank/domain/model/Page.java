package com.bank.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Builder
@Data
public class Page<T> {
    private List<T> data;
    private int page;
    private int size;
    private int totalCount;
}
