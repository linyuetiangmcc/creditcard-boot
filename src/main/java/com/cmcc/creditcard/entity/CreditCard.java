package com.cmcc.creditcard.entity;

import lombok.Data;

import java.util.Date;

@Data
public class CreditCard {
    private Integer cardId;
    private String cardName;
    private Date createTime;
    private Date updateTime;
    private String bankName;
    private Integer billDay;
    private Integer dueDay;
    private Integer userId;
    private Integer duetaskStatus;
    private String imageUrl;
}
