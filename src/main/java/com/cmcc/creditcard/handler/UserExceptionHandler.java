package com.cmcc.creditcard.handler;

import com.cmcc.creditcard.enums.ResultEnum;
import com.cmcc.creditcard.exception.UserAuthorizeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@ResponseBody
@Slf4j
public class UserExceptionHandler {
    @ExceptionHandler(value = UserAuthorizeException.class)
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    public Map<String, Object> handlerAuthorizeException(){
        Map<String, Object> modelMap = new HashMap<String, Object>();
        modelMap.put("success",false);
        modelMap.put("message", ResultEnum.LOGIN_FAIL);
        return modelMap;
    }
}
