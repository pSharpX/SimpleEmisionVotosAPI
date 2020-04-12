package edu.cibertec.votoelectronico.resource.communication;

import java.util.Collection;

import edu.cibertec.votoelectronico.dto.VotoDto;

public class ListVotoResponse extends PageResponse {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Collection<VotoDto> data;

	public ListVotoResponse() {
		super();
	}

	public ListVotoResponse(int currentPage, int pageSize, int totalPages, int totalItems, String orderBy,
			boolean orderByDesc, Collection<VotoDto> data) {
		super(currentPage, pageSize, totalPages, totalItems, orderBy, orderByDesc);
		this.setData(data);
	}

	public ListVotoResponse(Collection<VotoDto> data) {
		super();
		this.setData(data);
	}

	public ListVotoResponse(String message) {
		super(message);
	}

	public Collection<VotoDto> getData() {
		return data;
	}

	public void setData(Collection<VotoDto> data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "ListVotoResponse [data=" + data + "]";
	}

}
