package com.bidding.prediction.domain.persistence;

import java.util.Map;

public interface CoefficientRepository {

	Map<String, Double> getCoefficients(Map<String, String> features);

}
