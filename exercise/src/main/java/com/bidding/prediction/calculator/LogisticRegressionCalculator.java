package com.bidding.prediction.calculator;

import java.util.Map;

public interface LogisticRegressionCalculator {

	Double getLogisticRegression(Map<String, Double> coefficientsByFeature);

}
