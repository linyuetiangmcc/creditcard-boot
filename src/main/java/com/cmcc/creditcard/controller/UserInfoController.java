package com.cmcc.creditcard.controller;

import com.cmcc.creditcard.common.*;
import com.cmcc.creditcard.constant.CookieConstant;
import com.cmcc.creditcard.constant.RedisConstant;
import com.cmcc.creditcard.entity.UserInfo;
import com.cmcc.creditcard.entity.UserInfoApp;
import com.cmcc.creditcard.enums.ResultEnum;
import com.cmcc.creditcard.model.ResultMessage;
import com.cmcc.creditcard.model.WXSessionModel;
import com.cmcc.creditcard.service.CreditCardService;
import com.cmcc.creditcard.service.UserInfoService;
import com.cmcc.creditcard.utils.CookieUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/user")
public class UserInfoController {
    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private CreditCardService creditCardService;

    @GetMapping("/my")
    public Map<String, Object> my(){
        Map<String, Object> modelMap = new HashMap<String, Object>();
        Cookie cookie = getCookie();
        if(cookie == null){
            modelMap.put("success", false);
            modelMap.put("message", ResultEnum.LOGIN_FAIL);
            return modelMap;
        }

        String openid = redisTemplate.opsForValue().get(String.format(RedisConstant.TOKEN_PREFIX,cookie.getValue()));
        UserInfo userInfo = userInfoService.getUserInfoByOpenId(openid);
        if(userInfo != null){
            modelMap.put("user",userInfo);
        }
        modelMap.put("countDue",creditCardService.queryCreditCardCountDue(userInfo.getUserId()));
        modelMap.put("countNoDue",creditCardService.queryCreditCardCountNoDue(userInfo.getUserId()));
        return modelMap;
    }



    @PostMapping("/addUser")
    public  Map<String, Object> addUser(@RequestBody UserInfoApp userInfoApp) throws Exception {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        Cookie cookie = getCookie();
        if(cookie == null){
            modelMap.put("success", false);
            modelMap.put("message", ResultEnum.LOGIN_FAIL);
            return modelMap;
        }
        String openid = redisTemplate.opsForValue().get(String.format(RedisConstant.TOKEN_PREFIX,cookie.getValue()));
        if(openid == null){
            modelMap.put("success", false);
            modelMap.put("message", ResultEnum.LOGIN_FAIL);
            return modelMap;
        }
        if(userInfoService.getUserInfoByOpenId(openid) != null){
            modelMap.put("success", true);
            modelMap.put("message", ResultEnum.USER_EXIST);
            return modelMap;
        }
        UserInfo userInfo = new UserInfo();
        userInfo.setOpenId(openid);
        userInfo.setCreateTime(new Date());
        userInfo.setUpdateTime(new Date());
        userInfo.setAvatarUrl(userInfoApp.getAvatarUrl());
        userInfo.setCity(userInfoApp.getCity());
        userInfo.setCountry(userInfoApp.getCountry());
        userInfo.setGender(userInfoApp.getGender());
        userInfo.setLanguage(userInfoApp.getLanguage());
        userInfo.setNickName(userInfoApp.getNickName());
        userInfo.setProvince(userInfoApp.getProvince());
        userInfo.setStatus(1);
        if(userInfoService.addUserInfo(userInfo)) {
            modelMap.put("success", true);
            modelMap.put("message", ResultEnum.USER_ADDSUCCESS);
            return modelMap;
        }
        else {
            modelMap.put("success", true);
            modelMap.put("message", ResultEnum.USER_ADDFAIL);
            return modelMap;
        }
    }

    @PutMapping("/settel")
    public  Map<String, Object> settel(@RequestParam String telNumber) throws Exception {
        Map<String, Object> modelMap = new HashMap<String, Object>();

        if(!isMobile(telNumber)){
            modelMap.put("success", false);
            modelMap.put("message", ResultEnum.TEL_FORMATERROR);
            return modelMap;
        }

        Cookie cookie = getCookie();
        if(cookie == null){
            modelMap.put("success", false);
            modelMap.put("message", ResultEnum.LOGIN_FAIL);
            return modelMap;
        }
        String openid = redisTemplate.opsForValue().get(String.format(RedisConstant.TOKEN_PREFIX,cookie.getValue()));
        if(openid == null){
            modelMap.put("success", false);
            modelMap.put("message", ResultEnum.LOGIN_FAIL);
            return modelMap;
        }

        UserInfo userInfo = userInfoService.getUserInfoByOpenId(openid);
        userInfo.setTelNumber(telNumber);

        //System.out.println(userInfo);

        if(userInfoService.modifyUserInfo(userInfo)) {
            modelMap.put("success", true);
            modelMap.put("message", ResultEnum.SET_TEL_SUCCESS);
            return modelMap;
        }
        else {
            modelMap.put("success", true);
            modelMap.put("message", ResultEnum.SET_TEL_FAIL);
            return modelMap;
        }
    }

    private Cookie getCookie(){
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        return  CookieUtil.get(request,CookieConstant.TOKEN);
    }

    private boolean isMobile(String mobiles) {
        Pattern p = Pattern.compile("^1(3|5|7|8|4|6|9)\\d{9}");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }


}
