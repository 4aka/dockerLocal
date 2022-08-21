package com.atqc.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmailModel {

    private String email;
    private String referralCode;

    public static EmailModel create(String email, String referralCode) {

        return EmailModel.builder()
                .email(email)
                .referralCode(referralCode)
                .build();
    }
}
