package com.bidding.prediction.service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

import com.bidding.prediction.builder.FeatureNameBuilder;
import com.bidding.prediction.calculator.LogisticRegressionCalculator;
import com.bidding.prediction.domain.persistence.CoefficientRepository;

public class CalculatePredictionServiceImpl implements
		CalculatePredictionService {

	private CoefficientRepository coefficientRepository;
	private LogisticRegressionCalculator logisticRegressionCalculator;
	private FeatureNameBuilder featureNameBuilder;

	public CalculatePredictionServiceImpl(
			CoefficientRepository coefficientRepository,
			LogisticRegressionCalculator logisticRegressionCalculator,
			FeatureNameBuilder featureNameBuilder) {
		this.coefficientRepository = coefficientRepository;
		this.logisticRegressionCalculator = logisticRegressionCalculator;
		this.featureNameBuilder = featureNameBuilder;
	}

	@Override
	public BigDecimal predict(Map<String, String> features) {

		Set<String> featureNames = featureNameBuilder.getFeatureNames(features);

		Map<String, BigDecimal> coefficientsByFeature = coefficientRepository
				.getCoefficients(featureNames);

		return logisticRegressionCalculator
				.getLogisticRegression(coefficientsByFeature);
	}

}
