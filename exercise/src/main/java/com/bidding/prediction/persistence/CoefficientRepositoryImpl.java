package com.bidding.prediction.persistence;

import java.math.BigDecimal;
import java.util.List;
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

		String[] names = convertToStringArray(featureNames);

		List<String> coefficients = jedis.hmget(KEY_MODEL, names);

		for (int j = 0; j < names.length; j++) {
			fillMap(coefficientsByFeature, names[j], coefficients.get(j));
		}

		return coefficientsByFeature;
	}

	private String[] convertToStringArray(Set<String> featureNames) {
		String[] names = new String[featureNames.size()];
		int position = 0;
		for (String featureName : featureNames) {
			names[position] = featureName;
			position++;
		}
		return names;
	}

	private void fillMap(Map<String, BigDecimal> coefficientsByFeature,
			String featureName, String coefficient) {

		if (!StringUtils.isBlank(coefficient)) {
			coefficientsByFeature.put(featureName, new BigDecimal(coefficient));
		}

	}
}
