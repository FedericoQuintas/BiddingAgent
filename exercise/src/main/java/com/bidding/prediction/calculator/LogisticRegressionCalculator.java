package com.bidding.prediction.calculator;

import java.math.BigDecimal;
import java.util.Map;

public interface LogisticRegressionCalculator {

	BigDecimal getLogisticRegression(
			Map<String, BigDecimal> coefficientsByFeature);

}
