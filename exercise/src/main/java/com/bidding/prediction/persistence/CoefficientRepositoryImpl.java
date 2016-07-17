package com.bidding.prediction.persistence;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import redis.clients.jedis.Jedis;

import com.bidding.prediction.domain.persistence.CoefficientRepository;
import com.google.common.collect.Maps;

public class CoefficientRepositoryImpl implements CoefficientRepository {

	private static final String KEY_MODEL = "model";
	private Jedis jedis;

	public CoefficientRepositoryImpl(Jedis jedis) {
		this.jedis = jedis;

	}

	@Override
	public Map<String, BigDecimal> getCoefficients(Set<String> featureNames) {

		Map<String, BigDecimal> coefficientsByFeature = Maps.newHashMap();

		for (String featureName : featureNames) {
			String coefficient = jedis.hget(KEY_MODEL, featureName);
			fillMap(coefficientsByFeature, featureName, coefficient);
		}

		return coefficientsByFeature;
	}

	private void fillMap(Map<String, BigDecimal> coefficientsByFeature,
			String featureName, String coefficient) {

		if (!StringUtils.isBlank(coefficient)) {
			coefficientsByFeature.put(featureName, new BigDecimal(coefficient));
		}

	}
}
