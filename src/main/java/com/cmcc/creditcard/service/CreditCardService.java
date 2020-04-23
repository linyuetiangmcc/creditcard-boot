package com.cmcc.creditcard.service;

import com.cmcc.creditcard.entity.CreditCard;

import java.util.List;

public interface CreditCardService {
    List<CreditCard> queryCreditCardByUserId(Integer userId);
    List<CreditCard> queryCreditCardALL();
    CreditCard queryCreditCardByCardId(Integer cardId);
    boolean insertCreditCard(CreditCard creditCard);
    boolean updateCreditCard(CreditCard creditCard);
    boolean deletecreditCardByUserId(Integer userId);
    boolean deletecreditCardByCardId(Integer cardId);

    int queryCreditCardCountDue(Integer userId);
    int queryCreditCardCountNoDue(Integer userId);
}
