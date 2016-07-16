package com.bidding.prediction;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.bidding.calculator.LogisticRegressionCalculator;
import com.bidding.prediction.domain.persistence.CoefficientRepository;
import com.bidding.prediction.service.CalculatePredictionService;
import com.bidding.prediction.service.CalculatePredictionServiceImpl;
import com.google.common.collect.Maps;

public class CalculatePredictionServiceTest {

	@Mock
	private CoefficientRepository coefficientRepository;

	private CalculatePredictionService calculatePredictionService;

	private Map<String, String> features;

	@Mock
	private LogisticRegressionCalculator logisticRegressionCalculator;

	@Before
	public void before() {
		MockitoAnnotations.initMocks(this);
		features = Maps.newHashMap();
		calculatePredictionService = new CalculatePredictionServiceImpl(
				coefficientRepository, logisticRegressionCalculator);
	}

	@Test
	public void whenAsksForPredictionThenServiceRetrievesACoefficient() {

		Assert.assertNotNull(calculatePredictionService.predict(features));

	}

	@Test
	public void whenAsksForPredictionThenServiceObtainCoefficientsFromRepository() {

		calculatePredictionService.predict(features);

		verify(coefficientRepository).getCoefficients(features);

	}

	@Test
	public void whenAsksForPredictionThenServiceObtainResultFromLogisticRegressionCalculator() {

		Map<String, Double> coefficientsByFeature = Maps.newHashMap();

		when(coefficientRepository.getCoefficients(features)).thenReturn(
				coefficientsByFeature);

		calculatePredictionService.predict(features);

		verify(logisticRegressionCalculator).getLogisticRegression(
				coefficientsByFeature);

	}

	@Test
	public void whenCalculatorRetrievesANumberThenServiceRetrievesSameNumber() {

		Map<String, Double> coefficientsByFeature = Maps.newHashMap();

		when(
				logisticRegressionCalculator
						.getLogisticRegression(coefficientsByFeature))
				.thenReturn(new Double(10));

		Double result = calculatePredictionService.predict(features);

		Assert.assertEquals(new Double(10), result);

	}
}
