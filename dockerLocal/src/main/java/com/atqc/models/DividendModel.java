package com.atqc.models;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DividendModel {

    private String symbol;
    private String dividends;

    private float amount;
    private String currency;
    private String declaredDate;
    private String description;
    private String exDate;
    private String flag;
    private String frequency;
    private String paymentDate;
    private String recordDate;
    private int refid;
    private String id;
    private String key;
    private String subkey;
    private long date;
    private long updated;

}
