package com.intellectus.model.reports;

import com.intellectus.model.reports.items.BubbleChartItem;
import com.intellectus.model.reports.items.RiskDescription;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BubbleChartGraph {
    List<BubbleChartItem> bubbleChartItems;
    List<RiskDescription> riskDescriptions;

}
