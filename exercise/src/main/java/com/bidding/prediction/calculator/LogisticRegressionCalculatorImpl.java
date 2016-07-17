package com.bidding.prediction.calculator;

import java.math.BigDecimal;
import java.util.List;

public class LogisticRegressionCalculatorImpl implements
		LogisticRegressionCalculator {

	@Override
	public BigDecimal getLogisticRegression(
			List<BigDecimal> coefficientsByFeature) {

		BigDecimal total = sumCoefficients(coefficientsByFeature);
		return logisticFunction(total);
	}

	private BigDecimal sumCoefficients(List<BigDecimal> coefficientsByFeature) {
		BigDecimal total = new BigDecimal(0);
		for (BigDecimal coefficient : coefficientsByFeature) {
			total.add(coefficient);
		}
		return total;
	}

	private BigDecimal logisticFunction(BigDecimal total) {
		return null;
	}
}
