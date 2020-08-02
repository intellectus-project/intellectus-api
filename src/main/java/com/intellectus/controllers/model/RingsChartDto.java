package com.intellectus.controllers.model;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class RingsChartDto {
    private double sadness;
    private double happiness;
    private double fear;
    private double neutrality;
    private double anger;
    private long numberOfStats;
}
