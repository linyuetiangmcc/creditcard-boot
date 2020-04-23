package com.cmcc.creditcard.dao;

import com.cmcc.creditcard.entity.Bank;

import java.util.List;

public interface BankDao {
    List<Bank> queryBankList();
    Bank queryBankByBankIndex(Integer bankIndex);
}
