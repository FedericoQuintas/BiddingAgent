package com.bidding.prediction.integration;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.embedded.RedisServer;

import com.bidding.prediction.builder.AppendFeatureNameBuilder;
import com.bidding.prediction.calculator.LogisticRegressionCalculatorImpl;
import com.bidding.prediction.exception.InvalidFeaturesException;
import com.bidding.prediction.persistence.CoefficientRepositoryImpl;
import com.bidding.prediction.service.CalculatePredictionService;
import com.bidding.prediction.service.CalculatePredictionServiceImpl;
import com.bidding.prediction.validator.InvalidFeaturesValidator;
import com.google.common.collect.Maps;

public class CalculatePredictionServiceIntegrationTest {

	private static final double _6_21176449 = 6.21176449;

	private static final String BIAS = "bias";

	private static final String DEVICE_EXT_TYPE = "deviceExtType";

	private static final String TABLET = "tablet";

	private static final String DE = "de";

	private static final String _300X250 = "300x250";

	private static final String FIREFOX = "Firefox";

	private static final String BANNER_EXT_SIZE = "bannerExtSize";

	private static final String DEVICE_EXT_BROWSER = "deviceExtBrowser";

	private static final String DEVICE_LANGUAGE = "deviceLanguage";

	private static final String DEVICE_EXT_TYPE_TABLET = "deviceExtType=tablet";

	private static final String BANNER_EXT_SIZE_300X250 = "bannerExtSize=300x250";

	private static final String _0_7294739471 = "0.7294739471";

	private static final String _0_6282185905 = "0.6282185905";

	private static final String _0_1935418172 = "0.1935418172";

	private static final String _0_1131013246 = "0.1131013246";

	private static final String DEVICE_EXT_BROWSER_FIREFOX = "deviceExtBrowser=Firefox";

	private static final String DEVICE_LANGUAGE_DE = "deviceLanguage=de";

	private CalculatePredictionService calculatePredictionService;

	private Map<String, String> features;

	private static final String MODEL = "model";

	private static final String INVALID_FEATURES_INPUT = "Invalid features input";

	private static final int PORT = 6380;

	private static final String LOCALHOST = "localhost";

	private static Jedis jedis;

	private static RedisServer server;

	@SuppressWarnings("resource")
	@BeforeClass
	public static void beforeClass() throws IOException {

		server = new RedisServer(PORT);

		server.start();

		JedisPool pool = new JedisPool(new JedisPoolConfig(), LOCALHOST, PORT);

		jedis = pool.getResource();

	}

	@Before
	public void before() {

		jedis.flushAll();

		features = Maps.newHashMap();

		calculatePredictionService = new CalculatePredictionServiceImpl(new CoefficientRepositoryImpl(jedis),
				new LogisticRegressionCalculatorImpl(), new AppendFeatureNameBuilder(), new InvalidFeaturesValidator());
	}

	@Test
	public void whenUserAskForSpecificFeaturesThenCorrectResultIsRetrieved() {

		insertValue(DEVICE_LANGUAGE_DE, new BigDecimal(_0_1935418172).negate());

		insertValue(DEVICE_EXT_BROWSER_FIREFOX, new BigDecimal(_0_1131013246).negate());

		insertValue(BANNER_EXT_SIZE_300X250, new BigDecimal(_0_6282185905).negate());

		insertValue(DEVICE_EXT_TYPE_TABLET, new BigDecimal(_0_7294739471));
		
		insertValue(BIAS, new BigDecimal(_6_21176449).negate());

		features.put(DEVICE_LANGUAGE, DE);

		features.put(DEVICE_EXT_BROWSER, FIREFOX);
		
		features.put(BANNER_EXT_SIZE, _300X250);

		features.put(DEVICE_EXT_TYPE, TABLET);

		BigDecimal prediction = calculatePredictionService.predict(features);

		BigDecimal expectedCTR = new BigDecimal(0.0016306374);

		double delta = 0.0000000001d;
		
		Assert.assertEquals(expectedCTR.doubleValue(), prediction.doubleValue(), delta);

	}

	@Test
	public void whenAsksForPredictionWithEmptyFeaturesThenExceptionIsThrown() {

		try {
			calculatePredictionService.predict(features);
			fail();
		} catch (InvalidFeaturesException exception) {
			Assert.assertEquals(INVALID_FEATURES_INPUT, exception.getMessage());
		}

	}

	@Test
	public void whenAsksForPredictionWithNullFeaturesThenExceptionIsThrown() {

		try {
			calculatePredictionService.predict(null);
			fail();
		} catch (InvalidFeaturesException exception) {
			Assert.assertEquals(INVALID_FEATURES_INPUT, exception.getMessage());
		}

	}

	private void insertValue(String featureName, BigDecimal coefficient) {
		jedis.hset(MODEL, featureName, coefficient.toString());
	}

	@AfterClass
	public static void afterClass() {
		server.stop();
	}
}
