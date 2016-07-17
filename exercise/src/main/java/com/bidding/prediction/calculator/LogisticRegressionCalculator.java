package com.bidding.prediction.calculator;

import java.math.BigDecimal;
import java.util.List;

public interface LogisticRegressionCalculator {

	BigDecimal getLogisticRegression(List<BigDecimal> coefficients);

}
