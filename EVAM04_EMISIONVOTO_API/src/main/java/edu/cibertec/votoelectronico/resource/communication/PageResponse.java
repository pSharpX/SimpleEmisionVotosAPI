package edu.cibertec.votoelectronico.resource.communication;

public class PageResponse extends BaseResponse {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int currentPage;
	private int pageSize;
	private int totalPages;
	private int totalItems;
	private String orderBy;
	private boolean orderByDesc;

	private PageResponse(boolean success, String message) {
		super(success, message);
	}

	public PageResponse() {
	}

	public PageResponse(String message) {
		super(false, message);
	}

	public PageResponse(int currentPage, int pageSize, int totalPages, int totalItems, String orderBy,
			boolean orderByDesc) {
		this(true, "");
		this.currentPage = currentPage;
		this.pageSize = pageSize;
		this.totalPages = totalPages;
		this.totalItems = totalItems;
		this.orderBy = orderBy;
		this.orderByDesc = orderByDesc;
	}

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

}
