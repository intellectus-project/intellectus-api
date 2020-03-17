package com.atixlabs.model.reports.items;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RiskDescription {
    private String category;
    private String icon;
    private String risk;
    private SurveyIndex internalSurveyIndex;
    private SurveyIndex externalSurveyIndex;
    private SurveyIndex socialMediaIndex;
    private String rating;
    private String topWord;
    private List<String> words;

    public RiskDescription(String category, String risk, String icon, Double valueInternalSurveyIndex, Double valueExternalSurveyIndex, Double maxInternalSurveyIndex, Double maxExternalSurveyIndex){
        this.internalSurveyIndex = new SurveyIndex(maxInternalSurveyIndex,valueInternalSurveyIndex);
        this.externalSurveyIndex = new SurveyIndex(maxExternalSurveyIndex,valueExternalSurveyIndex );
        this.category = category;
        this.risk = risk;
        this.icon = icon;
        this.internalSurveyIndex = internalSurveyIndex;
        this.externalSurveyIndex = externalSurveyIndex;

    }
}
