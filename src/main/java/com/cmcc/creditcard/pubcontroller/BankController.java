package com.cmcc.creditcard.pubcontroller;

import com.cmcc.creditcard.service.BankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/bank")
public class BankController {
    @Autowired
    private BankService bankService;

    @RequestMapping("/getallbank")
    public Map<String, Object> getallbank()
    {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        /*List<String> result = new ArrayList<>();
        List<Bank> banks = bankService.queryBankList();
        for (Bank bank:banks
             ) {
            result.add(bank.getBankName());
        }
        modelMap.put("banklist",result);*/
        modelMap.put("banklist",bankService.queryBankList());
        return modelMap;
    }
}
