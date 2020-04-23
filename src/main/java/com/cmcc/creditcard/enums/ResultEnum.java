package com.cmcc.creditcard.enums;

import lombok.Getter;

@Getter
public enum ResultEnum {
    SUCCESS(0,"成功"),
    LOGIN_FAIL(1,"登录失败，登录信息不正确"),
    LOGOUT_SUCCESS(2,"登出成功"),
    ADDCARD_SUCCESS(3,"添加卡片成功"),
    ADDCARD_FAIL(4,"添加卡片失败"),
    USER_EXIST(5,"用户已存在"),
    USER_ADDSUCCESS(6,"添加用户成功"),
    USER_ADDFAIL(7,"添加用户失败"),
    TEL_FORMATERROR(8,"手机号码格式有误"),
    SET_TEL_SUCCESS(9,"设置手机成功"),
    SET_TEL_FAIL(10,"设置手机失败"),
    ;
    private Integer code;
    private String message;
    ResultEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
