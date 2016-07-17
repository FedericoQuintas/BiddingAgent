package com.bidding.prediction.resource;

import java.math.BigDecimal;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bidding.prediction.resource.request.PredictionRequest;
import com.bidding.prediction.service.CalculatePredictionService;

@Controller
public class CalculatePredictionResource {

	private CalculatePredictionService calculatePredictionService;

	public CalculatePredictionResource(
			CalculatePredictionService calculatePredictionService) {
		this.calculatePredictionService = calculatePredictionService;
	}

	@RequestMapping(method = RequestMethod.POST, consumes = { "application/json" })
	@ResponseBody
	public BigDecimal storeStock(@RequestBody PredictionRequest request) {
		return calculatePredictionService.predict(request.getFeatures());
	}

}
