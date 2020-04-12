package edu.cibertec.votoelectronico.service;

import java.util.List;

import edu.cibertec.votoelectronico.shared.Pagination;

public interface CommonService<T> {

	List<T> list();

	Pagination<T> list(int page, int size);

}
