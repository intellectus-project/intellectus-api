package com.intellectus.controllers.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class StatDto  {
    private double sadness;
    private double happiness;
    private double fear;
    private double neutrality;
    private double anger;
}