package com.bidding.prediction.builder;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.common.collect.Sets;

public class AppendFeatureNameBuilder implements FeatureNameBuilder {

	private static final String EQUAL = "=";

	@Override
	public Set<String> getFeatureNames(Map<String, String> features) {

		Set<String> featureNames = Sets.newHashSet();

		for (Entry<String, String> featureEntry : features.entrySet()) {

			String featureName = new StringBuilder()
					.append(featureEntry.getKey()).append(EQUAL)
					.append(featureEntry.getValue()).toString();

			featureNames.add(featureName);

		}

		return featureNames;
	}

}
