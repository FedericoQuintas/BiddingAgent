package com.bidding.prediction.unit;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.bidding.prediction.builder.FeatureNameBuilder;
import com.bidding.prediction.calculator.LogisticRegressionCalculator;
import com.bidding.prediction.domain.persistence.CoefficientRepository;
import com.bidding.prediction.service.CalculatePredictionService;
import com.bidding.prediction.service.CalculatePredictionServiceImpl;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class CalculatePredictionServiceUnitTest {

	private static final String BIAS = "bias";

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

		List<BigDecimal> coefficients = Lists
				.newArrayList(coefficientsByFeature.values());

		calculatePredictionService.predict(features);

		verify(logisticRegressionCalculator)
				.getLogisticRegression(coefficients);

	}

	@Test
	public void whenCalculatorRetrievesANumberThenServiceRetrievesSameNumber() {

		List<BigDecimal> coefficients = Lists.newArrayList();

		when(logisticRegressionCalculator.getLogisticRegression(coefficients))
				.thenReturn(new BigDecimal(10));

		BigDecimal result = calculatePredictionService.predict(features);

		Assert.assertEquals(new BigDecimal(10), result);

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void whenAsksForPredictionThenServiceAddsTheBiasCoefficient() {

		ArgumentCaptor<Set> coefficientsCaptor = ArgumentCaptor
				.forClass(Set.class);

		calculatePredictionService.predict(features);

		verify(coefficientRepository).getCoefficients(
				coefficientsCaptor.capture());

		Assert.assertTrue(coefficientsCaptor.getValue().contains(BIAS));

	}
}
