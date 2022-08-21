package com.atqc.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class StatsModel {

    /* float field is ignored because of name*/
    private String companyName;
    private long marketcap;
    private double week52high;
    private double week52low;
    private double week52highSplitAdjustOnly;
    private double week52lowSplitAdjustOnly;
    private long week52change;
    private long sharesOutstanding;
    private long avg10Volume;
    private long avg30Volume;
    private double day200MovingAvg;
    private double day50MovingAvg;
    private int employees;
    private long ttmDividendRate;
    private long dividendYield;
    private String nextDividendDate;
    private String exDividendDate;
    private String nextEarningsDate;
    private long peRatio;
    private long beta;
    private long maxChangePercent;
    private long year5ChangePercent;
    private long year2ChangePercent;
    private long year1ChangePercent;
    private long ytdChangePercent;
    private long month6ChangePercent;
    private long month3ChangePercent;
    private long month1ChangePercent;
    private long day30ChangePercent;
    private long day5ChangePercent;
    private long ttmEPS;

}
