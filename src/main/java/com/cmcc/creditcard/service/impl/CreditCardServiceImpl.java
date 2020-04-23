package com.cmcc.creditcard.service.impl;

import com.cmcc.creditcard.dao.CreditCardDao;
import com.cmcc.creditcard.entity.CreditCard;
import com.cmcc.creditcard.service.CreditCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class CreditCardServiceImpl implements CreditCardService {
    @Autowired
    private CreditCardDao creditCardDao;

    @Override
    public List<CreditCard> queryCreditCardByUserId(Integer userId) {
        return creditCardDao.queryCreditCardByUserId(userId);
    }

    @Override
    public List<CreditCard> queryCreditCardALL() {
        return creditCardDao.queryCreditCardALL();
    }

    @Override
    public CreditCard queryCreditCardByCardId(Integer cardId) {
        return creditCardDao.queryCreditCardByCardId(cardId);
    }

    @Override
    public boolean insertCreditCard(CreditCard creditCard) {
        int effectNumber = creditCardDao.insertCreditCard(creditCard);
        if(effectNumber == 1)
            return true;
        else
            return false;
    }

    @Override
    public boolean updateCreditCard(CreditCard creditCard) {
        int effectNumber = creditCardDao.updateCreditCard(creditCard);
        if(effectNumber == 1)
            return true;
        else
            return false;
    }

    @Override
    public boolean deletecreditCardByUserId(Integer userId) {
        int effectNumber = creditCardDao.deletecreditCardByUserId(userId);
        if(effectNumber >= 1)
            return true;
        else
            return false;
    }

    @Override
    public boolean deletecreditCardByCardId(Integer cardId) {
        int effectNumber = creditCardDao.deletecreditCardByCardId(cardId);
        if(effectNumber == 1)
            return true;
        else
            return false;
    }

    @Override
    public int queryCreditCardCountDue(Integer userId) {
        return creditCardDao.queryCreditCardCountDue(userId);
    }

    @Override
    public int queryCreditCardCountNoDue(Integer userId) {
        return creditCardDao.queryCreditCardCountNoDue(userId);
    }
}
