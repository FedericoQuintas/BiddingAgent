package com.bidding.prediction.calculator;

import java.math.BigDecimal;
import java.util.List;

public class LogisticRegressionCalculatorImpl implements LogisticRegressionCalculator {

	@Override
	public BigDecimal getLogisticRegression(List<BigDecimal> coefficients) {

		BigDecimal total = sumCoefficients(coefficients);

		return logisticFunction(total);
	}

	private BigDecimal sumCoefficients(List<BigDecimal> coefficients) {
		BigDecimal total = new BigDecimal(0);
		for (BigDecimal coefficient : coefficients) {
			total = total.add(coefficient);
		}
		return total;
	}

	private BigDecimal logisticFunction(BigDecimal total) {
		double functionResult = 1 / (1 + Math.exp(total.negate().doubleValue()));
		return new BigDecimal(functionResult);
	}
}
