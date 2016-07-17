package com.bidding.prediction.service;

import java.math.BigDecimal;
import java.util.Map;

public interface CalculatePredictionService {

	BigDecimal predict(Map<String, String> features);

}
