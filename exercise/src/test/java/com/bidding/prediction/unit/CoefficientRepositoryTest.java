package com.bidding.prediction.unit;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

import junit.framework.Assert;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.core.env.Environment;
import org.springframework.web.context.support.StandardServletEnvironment;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.embedded.RedisExecProvider;
import redis.embedded.RedisServer;
import redis.embedded.util.OS;

import com.bidding.prediction.domain.persistence.CoefficientRepository;
import com.bidding.prediction.persistence.CoefficientRepositoryImpl;
import com.google.common.collect.Sets;

public class CoefficientRepositoryTest {

	private static final String MODEL = "model";

	private static final int PORT = 6380;

	private static final String LOCALHOST = "localhost";

	private static final String REDIS_PATH = "REDIS_PATH";

	private static Jedis jedis;

	private static RedisServer server;

	private CoefficientRepository coefficientRepository;

	private Set<String> featureNames;

	private String featureName;

	private BigDecimal coefficient;

	@SuppressWarnings("resource")
	@BeforeClass
	public static void beforeClass() throws IOException {

		RedisExecProvider customProvider = RedisExecProvider.defaultProvider()
				.override(OS.UNIX, System.getenv().get(REDIS_PATH));
		// "/home/fede/redis-3.2.1/src/redis-server"
		server = new RedisServer(customProvider, PORT);

		server.start();

		JedisPool pool = new JedisPool(new JedisPoolConfig(), LOCALHOST, PORT);

		jedis = pool.getResource();

	}

	@Before
	public void before() {

		jedis.flushAll();

		coefficientRepository = new CoefficientRepositoryImpl(jedis);

		featureName = "deviceExtBrowser=Chrome";

		coefficient = new BigDecimal(0.2102317412);

		jedis.hset(MODEL, featureName, coefficient.toString());

		featureNames = Sets.newHashSet(featureName);
	}

	@Test
	public void whenACoefficientIsAskedThenJedisRetrievesTheCoefficient() {

		Map<String, BigDecimal> coefficients = coefficientRepository
				.getCoefficients(featureNames);

		Assert.assertEquals(coefficient, coefficients.get(featureName));

	}

	@Test
	public void whenTwoCoefficientsAreAskedThenJedisRetrievesBoth() {

		String secondFeatureName = "deviceLanguage=pl";

		BigDecimal secondCoefficient = new BigDecimal("1.266865912").negate();

		jedis.hset(MODEL, secondFeatureName, secondCoefficient.toString());

		featureNames.add(secondFeatureName);

		Map<String, BigDecimal> coefficients = coefficientRepository
				.getCoefficients(featureNames);

		Assert.assertEquals(coefficient, coefficients.get(featureName));

		Assert.assertEquals(secondCoefficient,
				coefficients.get(secondFeatureName));

	}

	@AfterClass
	public static void afterClass() {
		server.stop();
	}
}
