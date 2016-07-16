package com.bidding.prediction.persistence;

import java.util.Map;
import java.util.Set;

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
	public Map<String, Double> getCoefficients(Set<String> featureNames) {
		Map<String, Double> coefficientsByFeature = Maps.newHashMap();
		for (String featureName : featureNames) {
			String coefficient = jedis.hget(KEY_MODEL, featureName);
			coefficientsByFeature.put(featureName, Double.valueOf(coefficient));
		}
		return coefficientsByFeature;
	}

}
