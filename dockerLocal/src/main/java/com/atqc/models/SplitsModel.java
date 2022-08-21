package com.atqc.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SplitsModel {

    private String symbol;
    private String splits;

    @JsonIgnore
    private String declaredDate;
    private String description;
    private String exDate;
    private int fromFactor;
    private float ratio;
    private int refid;
    private int toFactor;
    private String id;
    private String key;
    private String subkey;
    private long date;
    private long updated;
}
