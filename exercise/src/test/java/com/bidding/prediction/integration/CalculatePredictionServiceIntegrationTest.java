package com.bidding.prediction.integration;

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
import com.bidding.prediction.persistence.CoefficientRepositoryImpl;
import com.bidding.prediction.service.CalculatePredictionService;
import com.bidding.prediction.service.CalculatePredictionServiceImpl;
import com.bidding.prediction.validator.InvalidFeaturesValidator;
import com.google.common.collect.Maps;

public class CalculatePredictionServiceIntegrationTest {

	private static final String DEVICE_EXT_TYPE_TABLET = "deviceExtType=tablet";

	private static final String BANNER_EXT_SIZE_800X250 = "bannerExtSize=800x250";

	private static final String _0_7294739471 = "0.7294739471";

	private static final String _0_1692596709 = "0.1692596709";

	private static final String _0_1935418172 = "0.1935418172";

	private static final String _0_1131013246 = "0.1131013246";

	private static final String DEVICE_EXT_BROWSER_FIREFOX = "deviceExtBrowser=Firefox";

	private static final String DEVICE_LANGUAGE_DE = "deviceLanguage=de";

	private CalculatePredictionService calculatePredictionService;

	private Map<String, String> features;

	private static final String MODEL = "model";

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

		insertValue(BANNER_EXT_SIZE_800X250, new BigDecimal(_0_1692596709));

		insertValue(DEVICE_EXT_TYPE_TABLET, new BigDecimal(_0_7294739471));

		BigDecimal prediction = calculatePredictionService.predict(features);

		BigDecimal expectedCTR = new BigDecimal(0.0016306374);

		Assert.assertEquals(expectedCTR, prediction);

	}

	private void insertValue(String featureName, BigDecimal coefficient) {
		jedis.hset(MODEL, featureName, coefficient.toString());
	}

	@AfterClass
	public static void afterClass() {
		server.stop();
	}
}
