package com.cmcc.creditcard.service;

import com.cmcc.creditcard.entity.Bank;

import java.util.List;

public interface BankService {
    List<Bank> queryBankList();
    Bank queryBankByBankIndex(Integer bankIndex);
}
