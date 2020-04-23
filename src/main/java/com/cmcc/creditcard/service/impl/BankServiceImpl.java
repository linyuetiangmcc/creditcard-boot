package com.cmcc.creditcard.service.impl;

import com.cmcc.creditcard.dao.BankDao;
import com.cmcc.creditcard.entity.Bank;
import com.cmcc.creditcard.service.BankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class BankServiceImpl implements BankService {
    @Autowired
    private BankDao bankDao;

    @Override
    public List<Bank> queryBankList() {
        return bankDao.queryBankList();
    }

    @Override
    public Bank queryBankByBankIndex(Integer bankIndex) {
        return bankDao.queryBankByBankIndex(bankIndex);
    }
}
