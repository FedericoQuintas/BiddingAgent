package com.bidding.prediction.service;

import java.util.Map;

import com.bidding.calculator.LogisticRegressionCalculator;
import com.bidding.prediction.domain.persistence.CoefficientRepository;

public class CalculatePredictionServiceImpl implements
		CalculatePredictionService {

	private CoefficientRepository coefficientRepository;
	private LogisticRegressionCalculator logisticRegressionCalculator;

	public CalculatePredictionServiceImpl(
			CoefficientRepository coefficientRepository,
			LogisticRegressionCalculator logisticRegressionCalculator) {
		this.coefficientRepository = coefficientRepository;
		this.logisticRegressionCalculator = logisticRegressionCalculator;
	}

	@Override
	public Double predict(Map<String, String> features) {

		Map<String, Double> coefficientsByFeature = coefficientRepository
				.getCoefficients(features);

		return logisticRegressionCalculator
				.getLogisticRegression(coefficientsByFeature);
	}

}
