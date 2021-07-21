package com.yataygecisle.preference.profiles.web.model;

import lombok.Getter;

@Getter
public enum AnalyzeCoefficient {
    TIMES_COEFFICIENT(30),
    BASKET(1.5);

    final double coefficient;

    AnalyzeCoefficient(double coefficient) {
        this.coefficient = coefficient;
    }
}
