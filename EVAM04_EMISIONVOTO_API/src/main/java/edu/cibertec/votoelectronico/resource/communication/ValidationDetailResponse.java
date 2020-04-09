package edu.cibertec.votoelectronico.resource.communication;

import java.util.Map;

public class ValidationDetailResponse extends BaseResponse {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Map<String, String> validations;

	private ValidationDetailResponse(boolean success, String message, Map<String, String> validations) {
		super(success, message);
		this.setValidations(validations);
	}

	public ValidationDetailResponse() {
		this(true, "", null);
	}

	public ValidationDetailResponse(String message) {
		this(false, message, null);
	}

	public ValidationDetailResponse(String message, Map<String, String> validations) {
		this(false, message, validations);
	}

	public Map<String, String> getValidations() {
		return validations;
	}

	public void setValidations(Map<String, String> validations) {
		this.validations = validations;
	}

	@Override
	public String toString() {
		return "ValidationResponse [validations=" + validations + ", isSuccess()=" + isSuccess() + ", getMessage()="
				+ getMessage() + "]";
	}

}
