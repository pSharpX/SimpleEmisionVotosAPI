package edu.cibertec.votoelectronico.dto;

import java.io.Serializable;

import javax.validation.constraints.Positive;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.QueryParam;

public class PageDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@QueryParam("page")
	@DefaultValue("1")
	@Positive
	private int page;

	@QueryParam("size")
	@DefaultValue("10")
	@Positive
	private int size;

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	@Override
	public String toString() {
		return "PageRequest{" + "page=" + page + ", size=" + size + '}';
	}

}
