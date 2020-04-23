package com.cmcc.creditcard.model;

import lombok.Data;

@Data
public class ResultMessage {
    private boolean success;
    private String message;
    private Integer code;
}
