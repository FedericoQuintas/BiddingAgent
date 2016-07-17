package com.bidding.prediction.unit;

import java.util.Map;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.bidding.prediction.builder.AppendFeatureNameBuilder;
import com.bidding.prediction.builder.FeatureNameBuilder;
import com.google.common.collect.Maps;

public class FeatureNameBuilderTest {

	private static final String EQUAL = "=";

	private FeatureNameBuilder featureNameBuilder;

	private Double coefficient;

	private String featureName;

	private Map<String, String> features;

	@Before
	public void before() {

		features = Maps.newHashMap();

		featureName = "deviceExtBrowser=Chrome";

		coefficient = new Double(0.2102317412);

		features.put(featureName, coefficient.toString());

		featureNameBuilder = new AppendFeatureNameBuilder();
	}

	@Test
	public void whenBuilderReceivesAKeyThenRetrievesAListWithCorrectName() {

		Set<String> featureNames = featureNameBuilder.getFeatureNames(features);

		String expectedFeatureName = new StringBuilder().append(featureName)
				.append(EQUAL).append(coefficient.toString()).toString();

		String name = featureNames.iterator().next();

		Assert.assertEquals(expectedFeatureName, name);
	}

	@Test
	public void whenBuilderReceivesAKeyThenRetrievesAListWithOneName() {

		Set<String> featureNames = featureNameBuilder.getFeatureNames(features);

		int expectedSize = 1;

		Assert.assertEquals(expectedSize, featureNames.size());

	}

	@Test
	public void whenBuilderReceivesTwoKeysThenRetrievesAListWithTwoNames() {

		String secondFeatureName = "bannerExtSize=0x0";

		Double secondCoefficient = new Double(1.616892819);

		features.put(secondFeatureName, secondCoefficient.toString());

		Set<String> featureNames = featureNameBuilder.getFeatureNames(features);

		int expectedSize = 2;

		Assert.assertEquals(expectedSize, featureNames.size());
	}

}
