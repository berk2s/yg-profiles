package com.yataygecisle.preference.profiles.services.impl;

import com.yataygecisle.preference.profiles.services.PossibilityService;
import com.yataygecisle.preference.profiles.web.model.AnalyzeCoefficient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.math.stat.descriptive.summary.Sum;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PossibilityServiceImpl implements PossibilityService {

    @Override
    public Double calculate(Long timesBasket) {
        Double minLimit = AnalyzeCoefficient.TIMES_COEFFICIENT.getCoefficient();
        Double basketCoefficient = AnalyzeCoefficient.BASKET.getCoefficient();

        Double multipliedTimesBasketByCoefficient = (timesBasket * basketCoefficient);

        Double calculatedTimes = (multipliedTimesBasketByCoefficient);

        Double possibility = (calculatedTimes / minLimit) * 100;

        if (possibility > 100)
            return 100.0;
        else
            return possibility;
    }

}
