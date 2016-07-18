package com.bidding.prediction.validator;

import java.util.Map;

public interface FeaturesValidator {

	void validate(Map<String, String> features);

}
