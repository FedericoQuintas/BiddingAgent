package com.bidding.prediction.domain.persistence;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

public interface CoefficientRepository {

	Map<String, BigDecimal> getCoefficients(Set<String> featureNames);

}
