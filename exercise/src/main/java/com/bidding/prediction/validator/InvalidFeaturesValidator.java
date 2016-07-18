package com.bidding.prediction.validator;

import java.util.Map;

import com.bidding.prediction.exception.InvalidFeaturesException;

public class InvalidFeaturesValidator implements FeaturesValidator {

	private static final String INVALID_FEATURES_INPUT = "Invalid features input";

	@Override
	public void validate(Map<String, String> features) {
		if (features == null || features.isEmpty()) {
			throw new InvalidFeaturesException(INVALID_FEATURES_INPUT);
		}

	}

}
