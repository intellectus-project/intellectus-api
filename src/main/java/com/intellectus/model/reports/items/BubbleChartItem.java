package com.intellectus.model.reports.items;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BubbleChartItem {
    private String country;
    private double riskVulnerability;
    private double businessImpact;
    private double bubbleSize;
    private String rating;
}
