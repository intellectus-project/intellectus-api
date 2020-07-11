package com.intellectus.model.reports;

import com.intellectus.model.reports.items.CountryItem;
import com.intellectus.model.reports.items.PieChartItem;
import com.intellectus.model.reports.items.RiskDescription;
import lombok.Setter;

import java.util.List;

@Setter
public class PieChartGraph {
    private String title;
    private List<PieChartItem> items;
    private List<CountryItem> countryItems;
    private List<RiskDescription> riskDescriptions;

}
