package edu.cibertec.votoelectronico.client;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public interface ListAsyncHttpClient {

	public <T, E> List<CompletableFuture<T>> requestAsync(String path, REQUEST_METHOD method, List<E> collectionEntity,
			Map<String, Object> header, Class<T> responseType, int requestCount);

	public <T, E> List<CompletionStage<T>> requestNativeAsync(String path, REQUEST_METHOD method, List<E> collectionEntity,
			Map<String, Object> header, Class<T> responseType, int requestCount);

}
