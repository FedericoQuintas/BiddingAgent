package com.bidding.prediction.builder;

import java.util.Map;
import java.util.Set;

public interface FeatureNameBuilder {

	Set<String> getFeatureNames(Map<String, String> features);

}
