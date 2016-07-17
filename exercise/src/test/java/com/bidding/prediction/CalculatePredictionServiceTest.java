package com.bidding.prediction;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.bidding.prediction.builder.FeatureNameBuilder;
import com.bidding.prediction.calculator.LogisticRegressionCalculator;
import com.bidding.prediction.domain.persistence.CoefficientRepository;
import com.bidding.prediction.service.CalculatePredictionService;
import com.bidding.prediction.service.CalculatePredictionServiceImpl;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class CalculatePredictionServiceTest {

	@Mock
	private CoefficientRepository coefficientRepository;

	private CalculatePredictionService calculatePredictionService;

	private Map<String, String> features;

	@Mock
	private LogisticRegressionCalculator logisticRegressionCalculator;

	@Mock
	private FeatureNameBuilder featureNameBuilder;

	@Before
	public void before() {
		MockitoAnnotations.initMocks(this);
		features = Maps.newHashMap();
		calculatePredictionService = new CalculatePredictionServiceImpl(
				coefficientRepository, logisticRegressionCalculator,
				featureNameBuilder);
	}

	@Test
	public void whenAsksForPredictionThenServiceRetrievesACoefficient() {

		Assert.assertNotNull(calculatePredictionService.predict(features));

	}

	@Test
	public void whenAsksForPredictionThenServiceObtainFeatureNamesFromBuilder() {

		calculatePredictionService.predict(features);

		verify(featureNameBuilder).getFeatureNames(features);

	}

	@Test
	public void whenAsksForPredictionThenServiceObtainCoefficientsFromRepository() {

		Set<String> featureNames = Sets.newHashSet();

		when(featureNameBuilder.getFeatureNames(features)).thenReturn(
				featureNames);

		calculatePredictionService.predict(features);

		verify(coefficientRepository).getCoefficients(featureNames);

	}

	@Test
	public void whenAsksForPredictionThenServiceObtainResultFromLogisticRegressionCalculator() {

		Map<String, BigDecimal> coefficientsByFeature = Maps.newHashMap();

		Set<String> featureNames = Sets.newHashSet();

		when(featureNameBuilder.getFeatureNames(features)).thenReturn(
				featureNames);

		when(coefficientRepository.getCoefficients(featureNames)).thenReturn(
				coefficientsByFeature);

		calculatePredictionService.predict(features);

		verify(logisticRegressionCalculator).getLogisticRegression(
				coefficientsByFeature);

	}

	@Test
	public void whenCalculatorRetrievesANumberThenServiceRetrievesSameNumber() {

		Map<String, BigDecimal> coefficientsByFeature = Maps.newHashMap();

		when(
				logisticRegressionCalculator
						.getLogisticRegression(coefficientsByFeature))
				.thenReturn(new BigDecimal(10));

		BigDecimal result = calculatePredictionService.predict(features);

		Assert.assertEquals(new BigDecimal(10), result);

	}
}
