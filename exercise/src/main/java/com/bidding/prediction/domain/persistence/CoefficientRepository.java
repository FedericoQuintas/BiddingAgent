package com.bidding.prediction.domain.persistence;

import java.util.Map;
import java.util.Set;

public interface CoefficientRepository {

	Map<String, Double> getCoefficients(Set<String> featureNames);
	
}
