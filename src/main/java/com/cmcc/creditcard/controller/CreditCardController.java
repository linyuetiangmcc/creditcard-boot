package com.cmcc.creditcard.controller;

import com.cmcc.creditcard.constant.CookieConstant;
import com.cmcc.creditcard.constant.RedisConstant;
import com.cmcc.creditcard.entity.Bank;
import com.cmcc.creditcard.entity.CreditCard;
import com.cmcc.creditcard.entity.CreditCardApp;
import com.cmcc.creditcard.entity.UserInfo;
import com.cmcc.creditcard.enums.ResultEnum;
import com.cmcc.creditcard.service.BankService;
import com.cmcc.creditcard.service.CreditCardService;
import com.cmcc.creditcard.service.UserInfoService;
import com.cmcc.creditcard.utils.CookieUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/card")
public class CreditCardController {
    @Autowired
    private CreditCardService creditCardService;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private BankService bankService;

    @GetMapping("/list")
    public Map<String, Object> list(){
        Map<String, Object> modelMap = new HashMap<String, Object>();
/*        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();*/
        Cookie cookie = getCookie();
        if(cookie == null){
            return null;
        }
        String openid = redisTemplate.opsForValue().get(String.format(RedisConstant.TOKEN_PREFIX,cookie.getValue()));
        UserInfo userInfo = userInfoService.getUserInfoByOpenId(openid);

        List<CreditCard> creditCardList = creditCardService.queryCreditCardByUserId(userInfo.getUserId());

        if(creditCardList != null){
            modelMap.put("success",true);
            modelMap.put("creditCardList",creditCardList);
        }
        return modelMap;
    }

    @PostMapping("/add")
    public Map<String, Object> add(@Valid @RequestBody CreditCardApp creditCardApp,BindingResult bindingResult){
        Map<String, Object> modelMap = new HashMap<String, Object>();
        Cookie cookie = getCookie();
        if(cookie == null){
            modelMap.put("success", false);
            modelMap.put("message", ResultEnum.LOGIN_FAIL);
            return modelMap;
        }

        if(bindingResult.hasErrors()){
            //System.out.println(bindingResult.getFieldError().getDefaultMessage());
            modelMap.put("success", false);
            modelMap.put("message",bindingResult.getFieldError().getDefaultMessage());
            return modelMap;
        }

        String openid = redisTemplate.opsForValue().get(String.format(RedisConstant.TOKEN_PREFIX,cookie.getValue()));
        UserInfo userInfo = userInfoService.getUserInfoByOpenId(openid);
        Bank bank = bankService.queryBankByBankIndex(creditCardApp.getBankNameIndex());

        CreditCard creditCard = new CreditCard();
        creditCard.setUserId(userInfo.getUserId());
        //creditCard.setBackNumber("8888");
        creditCard.setCardName(creditCardApp.getCardName());
        creditCard.setBankName(bank.getBankName());
        creditCard.setBillDay(creditCardApp.getBillDayIndex() + 1);
        creditCard.setDueDay(getDuedateCurrent(creditCardApp.getBillDayIndex() + 1,creditCardApp.getBankNameIndex()));
        creditCard.setCreateTime(new Date());
        creditCard.setUpdateTime(new Date());
        creditCard.setDuetaskStatus(0);
        creditCard.setImageUrl(bank.getImageUrl());
        modelMap.put("success", creditCardService.insertCreditCard(creditCard));
        return modelMap;
    }

    @PostMapping("/duecard")
    public Map<String, Object> duecard(Integer cardId){
        Map<String, Object> modelMap = new HashMap<String, Object>();
        CreditCard creditCard = creditCardService.queryCreditCardByCardId(cardId);
        creditCard.setDuetaskStatus(1);
        modelMap.put("success", creditCardService.updateCreditCard(creditCard));
        return modelMap;
    }

    @DeleteMapping("/delete")
    public Map<String, Object> deletecard(Integer cardId){
        Map<String, Object> modelMap = new HashMap<String, Object>();
        modelMap.put("success", creditCardService.deletecreditCardByCardId(cardId));
        return modelMap;
    }

    /*
    账单日算出当前还款日
     */
    private Integer getDuedateCurrent(Integer billDay,Integer bankIndex){
        Integer gracePeriod = bankService.queryBankByBankIndex(bankIndex).getGracePeriod();

        LocalDate today = LocalDate.now();
        LocalDate lastDay =today.with(TemporalAdjusters.lastDayOfMonth());
        if((billDay + gracePeriod) <= lastDay.getDayOfMonth()) {
            //System.out.println(billDay + gracePeriod);
            return billDay + gracePeriod;
        }
        else{
            LocalDate lastmonth = today.minusMonths(1);
            LocalDate lastbillday  = lastmonth.withDayOfMonth(billDay);
            LocalDate dueday = lastbillday.plusDays(gracePeriod);
            //System.out.println(dueday.getDayOfMonth());
            return dueday.getDayOfMonth();
        }

    }

    private Cookie getCookie(){
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        return  CookieUtil.get(request,CookieConstant.TOKEN);
    }

}
