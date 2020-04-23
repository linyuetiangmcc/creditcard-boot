package com.cmcc.creditcard.aspect;

import com.cmcc.creditcard.constant.CookieConstant;
import com.cmcc.creditcard.constant.RedisConstant;
import com.cmcc.creditcard.exception.UserAuthorizeException;
import com.cmcc.creditcard.utils.CookieUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
@Slf4j
public class UserAuthorizeAspect {
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Pointcut("execution(public * com.cmcc.creditcard.controller.*.*(..))" +
            "&& !execution(public * com.cmcc.creditcard.controller.UserLoginAction.*(..))")
    public void verify(){
    }

    @Before("verify()")
    public void doVerify(){
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        Cookie cookie = CookieUtil.get(request,CookieConstant.TOKEN);
        if(cookie == null){
            log.warn("[登录校验]Cookie中查不到token");
            throw new UserAuthorizeException();
        }

        //System.out.println(cookie.getValue());

        String tokenValue = redisTemplate.opsForValue().get(String.format(RedisConstant.TOKEN_PREFIX,cookie.getValue()));
        if(StringUtils.isEmpty(tokenValue)){
            log.warn("[登录校验]Redis中查不到token");
            throw new UserAuthorizeException();
        }
    }

}
