package com.atixlabs.model.reports;

import com.atixlabs.model.reports.items.CountryItem;
import com.atixlabs.model.reports.items.PieChartItem;
import com.atixlabs.model.reports.items.RiskDescription;
import lombok.Setter;

import java.util.List;

@Setter
public class PieChartGraph {
    private String title;
    private List<PieChartItem> items;
    private List<CountryItem> countryItems;
    private List<RiskDescription> riskDescriptions;

}
