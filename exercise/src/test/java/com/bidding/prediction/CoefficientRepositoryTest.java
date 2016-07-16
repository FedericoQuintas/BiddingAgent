package com.bidding.prediction;

import java.util.Map;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import com.bidding.prediction.domain.persistence.CoefficientRepository;
import com.google.common.collect.Sets;

public class CoefficientRepositoryTest {

	private static final String LOCALHOST = "localhost";

	private Jedis jedis;

	private CoefficientRepository coefficientRepository;

	@SuppressWarnings("resource")
	@BeforeClass
	public void beforeClass() {

		JedisPool pool = new JedisPool(new JedisPoolConfig(), LOCALHOST);

		jedis = pool.getResource();

	}

	@Before
	public void before() {
		jedis.flushAll();
	}

	@Test
	public void whenACoefficientIsAskedThenJedisRetrievesTheCoefficient() {

		String featureName = "deviceExtBrowser=Chrome";

		Double coefficient = new Double(0.2102317412);

		jedis.set(featureName, coefficient.toString());

		Map<String, Double> coefficients = coefficientRepository
				.getCoefficients(Sets.newHashSet(featureName));

		Assert.assertEquals(new Double(0.2102317412),
				coefficients.get(featureName));

	}

}
