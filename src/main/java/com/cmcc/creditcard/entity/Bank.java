package com.cmcc.creditcard.entity;

import lombok.Data;
@Data
public class Bank {
    private Integer bankId;
    private Integer bankIndex;
    private String bankName;
    private Integer gracePeriod;
    private String imageUrl;
}
