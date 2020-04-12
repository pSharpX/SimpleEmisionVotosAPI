package edu.cibertec.votoelectronico.shared;

import java.util.List;

public class Pagination<T> {

	private int currentPage;
	private int pageSize;
	private int totalPages;
	private int totalItems;
	private String orderBy;
	private boolean orderByDesc;
	private List<T> result;

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public int getTotalItems() {
		return totalItems;
	}

	public void setTotalItems(int totalItems) {
		this.totalItems = totalItems;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public boolean isOrderByDesc() {
		return orderByDesc;
	}

	public void setOrderByDesc(boolean orderByDesc) {
		this.orderByDesc = orderByDesc;
	}

	public List<T> getResult() {
		return result;
	}

	public void setResult(List<T> result) {
		this.result = result;
	}

}
