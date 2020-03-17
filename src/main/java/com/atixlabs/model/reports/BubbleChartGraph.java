package com.atixlabs.model.reports;

import com.atixlabs.model.reports.items.BubbleChartItem;
import com.atixlabs.model.reports.items.RiskDescription;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BubbleChartGraph {
    List<BubbleChartItem> bubbleChartItems;
    List<RiskDescription> riskDescriptions;

}
