package com.bidding.prediction.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.bidding.prediction.builder.FeatureNameBuilder;
import com.bidding.prediction.calculator.LogisticRegressionCalculator;
import com.bidding.prediction.domain.persistence.CoefficientRepository;
import com.bidding.prediction.validator.FeaturesValidator;
import com.google.common.collect.Lists;

public class CalculatePredictionServiceImpl implements CalculatePredictionService {

	private static final String BIAS = "bias";
	private CoefficientRepository coefficientRepository;
	private LogisticRegressionCalculator logisticRegressionCalculator;
	private FeatureNameBuilder featureNameBuilder;
	private FeaturesValidator featuresValidator;

	public CalculatePredictionServiceImpl(CoefficientRepository coefficientRepository,
			LogisticRegressionCalculator logisticRegressionCalculator, FeatureNameBuilder featureNameBuilder,
			FeaturesValidator featuresValidator) {
		this.coefficientRepository = coefficientRepository;
		this.logisticRegressionCalculator = logisticRegressionCalculator;
		this.featureNameBuilder = featureNameBuilder;
		this.featuresValidator = featuresValidator;
	}

	@Override
	public BigDecimal predict(Map<String, String> features) {

		validate(features);

		Set<String> featureNames = featureNameBuilder.getFeatureNames(features);

		addBiasCoefficient(featureNames);

		Map<String, BigDecimal> coefficientsByFeature = coefficientRepository.getCoefficients(featureNames);

		List<BigDecimal> coefficients = obtainCoefficients(coefficientsByFeature);

		return logisticRegressionCalculator.getLogisticRegression(coefficients);
	}

	private void validate(Map<String, String> features) {
		featuresValidator.validate(features);

	}

	private List<BigDecimal> obtainCoefficients(Map<String, BigDecimal> coefficientsByFeature) {
		List<BigDecimal> coefficients = Lists.newArrayList(coefficientsByFeature.values());
		return coefficients;
	}

	private void addBiasCoefficient(Set<String> featureNames) {
		featureNames.add(BIAS);
	}

}
