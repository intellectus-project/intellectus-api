package com.atixlabs.model.reports.items;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
public class BubbleChartItem {
    private String country;
    private double riskVulnerability;
    private double businessImpact;
    private double bubbleSize;
    private String rating;
}
