package com.bidding.prediction.resource.request;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PredictionRequest {

	@JsonProperty
	private Map<String, String> features;

	public void setFeatures(Map<String, String> features) {
		this.features = features;
	}

	public Map<String, String> getFeatures() {
		return features;
	}

}
