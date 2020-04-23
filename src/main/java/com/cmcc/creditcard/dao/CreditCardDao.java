package com.cmcc.creditcard.dao;

import com.cmcc.creditcard.entity.CreditCard;

import java.util.List;

public interface CreditCardDao {
    //List<CreditCard> queryCreditCardList();
    List<CreditCard> queryCreditCardByUserId(Integer userId);
    List<CreditCard> queryCreditCardALL();
    CreditCard queryCreditCardByCardId(Integer cardId);
    int insertCreditCard(CreditCard creditCard);
    int updateCreditCard(CreditCard creditCard);
    int deletecreditCardByUserId(Integer userId);
    int deletecreditCardByCardId(Integer cardId);

    int queryCreditCardCountDue(Integer userId);
    int queryCreditCardCountNoDue(Integer userId);

}
