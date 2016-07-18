package com.bidding.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import com.bidding.prediction.builder.AppendFeatureNameBuilder;
import com.bidding.prediction.builder.FeatureNameBuilder;
import com.bidding.prediction.calculator.LogisticRegressionCalculator;
import com.bidding.prediction.calculator.LogisticRegressionCalculatorImpl;
import com.bidding.prediction.domain.persistence.CoefficientRepository;
import com.bidding.prediction.persistence.CoefficientRepositoryImpl;
import com.bidding.prediction.resource.CalculatePredictionResource;
import com.bidding.prediction.service.CalculatePredictionService;
import com.bidding.prediction.service.CalculatePredictionServiceImpl;
import com.bidding.prediction.validator.FeaturesValidator;
import com.bidding.prediction.validator.InvalidFeaturesValidator;

@Configuration
@EnableWebMvc
public class AppConfiguration {

	private static final String LOCALHOST = "localhost";

	@Bean
	public CalculatePredictionService calculatePredictionService(CoefficientRepository coefficientRepository,
			LogisticRegressionCalculator logisticRegressionCalculator, FeatureNameBuilder featureNameBuilder,
			FeaturesValidator featuresValidator) {
		return new CalculatePredictionServiceImpl(coefficientRepository, logisticRegressionCalculator,
				featureNameBuilder, featuresValidator);
	}

	@Bean
	public FeatureNameBuilder featureNameBuilder() {
		return new AppendFeatureNameBuilder();
	}

	@Bean
	public FeaturesValidator featuresValidator() {
		return new InvalidFeaturesValidator();
	}

	@Bean
	public LogisticRegressionCalculator logisticRegressionCalculator() {
		return new LogisticRegressionCalculatorImpl();
	}

	@SuppressWarnings("resource")
	@Bean
	public CoefficientRepository coefficientRepository() {

		Jedis jedis = new JedisPool(new JedisPoolConfig(), LOCALHOST, 6379).getResource();

		return new CoefficientRepositoryImpl(jedis);
	}

	@Bean
	public CalculatePredictionResource calculatePredictionResource(CalculatePredictionService calculatePredictionService) {
		return new CalculatePredictionResource(calculatePredictionService);
	}

}
