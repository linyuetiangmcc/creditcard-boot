package com.cmcc.creditcard.entity;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

@Data
public class CreditCardApp {

    @NotEmpty(message = "卡片名必填")
    private String cardName;
    private Integer bankNameIndex;
    private Integer billDayIndex;
}
