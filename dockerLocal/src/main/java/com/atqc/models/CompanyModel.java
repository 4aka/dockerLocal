package com.atqc.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CompanyModel {

    private String symbol;
    private String companyName;
    private int employees;
    private String industry;

    private String CEO;
    private String exchange;
    private String issueType;
    private String sector;
    private String securityName;
    private String dividendYield;
    private String marketCapitalization;
    private String description;
    private String logoLink;
    private String address;
    private String address2;
    private String state;
    private String city;
    private String zip;
    private String country;
    private String phone;
    private String website;
    private String primarySicCode;
    private List<String> tags;
    private StatsModel stats;
}
