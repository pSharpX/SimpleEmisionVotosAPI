package edu.cibertec.votoelectronico.resource.communication;

import java.util.Map;

import edu.cibertec.votoelectronico.dto.VotoDto;

public class EmisionVotoResponse extends ValidationDetailResponse {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private VotoDto data;

	public EmisionVotoResponse() {
		super();
	}

	public EmisionVotoResponse(VotoDto data) {
		super();
		this.setData(data);
	}

	public EmisionVotoResponse(String message) {
		super(message);
	}

	public EmisionVotoResponse(String message, Map<String, String> validations) {
		super(message, validations);
	}

	public VotoDto getData() {
		return data;
	}

	public void setData(VotoDto data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "EmisionVotoResponse [data=" + data + ", toString()=" + super.toString() + "]";
	}

}
