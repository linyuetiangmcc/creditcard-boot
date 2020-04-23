package com.cmcc.creditcard.controller;
import com.cmcc.creditcard.common.*;

import com.cmcc.creditcard.constant.CookieConstant;
import com.cmcc.creditcard.constant.RedisConstant;
import com.cmcc.creditcard.exception.UserAuthorizeException;
import com.cmcc.creditcard.model.WXSessionModel;
import com.cmcc.creditcard.utils.CookieUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/user")
public class UserLoginAction {
    @Autowired
    private StringRedisTemplate redisTemplate;

    @PostMapping("/wxLogin")
    public Map<String, Object> wxLogin(String code, HttpServletResponse response) throws Exception {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        String url = "https://api.weixin.qq.com/sns/jscode2session";
        Map<String, String> param = new HashMap<>();
        param.put("appid", "wxf6757774d137693f");
        param.put("secret", "8adb70bf336fb3572284c74656d624f5");
        param.put("js_code", code);
        param.put("grant_type", "authorization_code");
        String wxResult = HttpClientUtil.doGet(url, param);
        WXSessionModel model = JsonUtils.jsonToPojo(wxResult, WXSessionModel.class);


        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        Cookie cookie = CookieUtil.get(request, CookieConstant.TOKEN);
        //带有token的cookie，更新时间。
        if(cookie != null){
            redisTemplate.opsForValue().set(String.format(RedisConstant.TOKEN_PREFIX,cookie.getValue()),model.getOpenid(),RedisConstant.EXPIRE, TimeUnit.SECONDS);
            modelMap.put("success",true);
            return modelMap;
        }


        //没有登陆，写入cookie键为token前缘，值为openid
        String token = UUID.randomUUID().toString();
        Integer expire = RedisConstant.EXPIRE;
        redisTemplate.opsForValue().set(String.format(RedisConstant.TOKEN_PREFIX,token),model.getOpenid(),expire, TimeUnit.SECONDS);
        CookieUtil.set(response, CookieConstant.TOKEN,token,expire);
        //刷新session-key 到redis
        redisTemplate.opsForValue().set("user-redis-session:" + model.getOpenid(),model.getSession_key(),expire,TimeUnit.SECONDS);
        modelMap.put("success",true);
        return modelMap;
    }
}
