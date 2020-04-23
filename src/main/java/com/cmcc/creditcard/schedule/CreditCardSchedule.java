package com.cmcc.creditcard.schedule;

import com.cmcc.creditcard.entity.CreditCard;
import com.cmcc.creditcard.service.CreditCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class CreditCardSchedule {

    @Autowired
    private CreditCardService creditCardService;

    @Scheduled(cron="0 0 0 1 * ?")
    //@Scheduled(fixedDelay = 3000)
    public void updateDuetaskStatus(){
        List<CreditCard> creditCardList = creditCardService.queryCreditCardALL();
        for (CreditCard creditCard:creditCardList
             ) {
            if(creditCard.getDuetaskStatus() == 1){
                creditCard.setDuetaskStatus(0);
                creditCardService.updateCreditCard(creditCard);
            }
        }
        System.out.println("update dueTackStatus to 1 of tb_card success");
    }
}
