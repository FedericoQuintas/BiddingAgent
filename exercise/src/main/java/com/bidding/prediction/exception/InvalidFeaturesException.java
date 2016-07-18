package com.bidding.prediction.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Illegal argument")
public class InvalidFeaturesException extends IllegalArgumentException {

	private static final long serialVersionUID = -8217557407191232687L;

	public InvalidFeaturesException(String invalidArgument) {
		super(invalidArgument);
	}

}
