package com.bidding.prediction.service;

import java.util.Map;

public interface CalculatePredictionService {

	Double predict(Map<String, String> features);

}
