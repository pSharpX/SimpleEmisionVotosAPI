package edu.cibertec.votoelectronico.client;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
public class JAXRSAsyncHttpClient implements CommonAsyncHttpClient {

	private final Logger LOG = LoggerFactory.getLogger(JAXRSAsyncHttpClient.class);

	private Client client;
	private ExecutorService executor;

	@PostConstruct
	private void initClient() {
		executor = Executors.newFixedThreadPool(10);
		client = ClientBuilder.newBuilder().build();
	}

	@PreDestroy
	private void closeClient() {
		if (client != null)
			client.close();
		if (executor != null && !executor.isShutdown())
			executor.shutdown();
	}

	private Builder configureTarget(String path, Map<String, Object> header) {
		Builder builder = client.target(path).request().accept(MediaType.APPLICATION_JSON);
		if (!Objects.isNull(header) && !header.isEmpty()) {
			header.forEach((key, value) -> {
				builder.header(key, value);
			});
		}
		return builder;
	}

	private <E, T> T invokeTarget(REQUEST_METHOD method, Builder builder, E entity, Class<T> responseType) {
		Response response = null;
		switch (method) {
		case GET:
			response = builder.get();
			break;
		case POST:
			response = builder.post(Entity.json(entity));
			break;
		case PUT:
			response = builder.put(Entity.json(entity));
			break;
		case DELETE:
			response = builder.delete();
			break;
		default:
			response = builder.get();
			break;
		}
		return response.readEntity(responseType);
	}

	private <E, T> CompletionStage<T> invokeTargetAsync(REQUEST_METHOD method, Builder builder, E entity,
			Class<T> responseType) {
		CompletionStage<T> response = null;
		switch (method) {
		case GET:
			response = builder.rx().get(responseType);
			break;
		case POST:
			response = builder.rx().post(Entity.json(entity), responseType);
			break;
		case PUT:
			response = builder.rx().put(Entity.json(entity), responseType);
			break;
		case DELETE:
			response = builder.rx().delete(responseType);
			break;
		default:
			response = builder.rx().get(responseType);
			break;
		}
		return response;
	}

	@Override
	public <T, E> CompletableFuture<T> requestAsync(String path, REQUEST_METHOD method, E entity,
			Map<String, Object> header, Class<T> responseType) {
		return CompletableFuture.supplyAsync(() -> {
			Builder builder = configureTarget(path, header);

			T value = invokeTarget(method, builder, entity, responseType);
			LOG.info("[{}]", Thread.currentThread().getId());
			LOG.info(value.toString());
			return value;
		});
	}

	@Override
	public <T, E> List<CompletableFuture<T>> requestAsync(String path, REQUEST_METHOD method, List<E> collectionEntity,
			Map<String, Object> header, Class<T> responseType, int requestCount) {
		if (collectionEntity != null && collectionEntity.size() != requestCount)
			throw new IllegalArgumentException("Collection's size and request count must be the same");
		return IntStream
				.range(0, requestCount).mapToObj(i -> CompletableFuture.supplyAsync(() -> invokeTarget(method,
						configureTarget(path, header), collectionEntity.get(i), responseType), executor))
				.collect(Collectors.toList());
	}

	@Override
	public <T, E> List<CompletionStage<T>> requestNativeAsync(String path, REQUEST_METHOD method,
			List<E> collectionEntity, Map<String, Object> header, Class<T> responseType, int requestCount) {
		if (collectionEntity != null && collectionEntity.size() != requestCount)
			throw new IllegalArgumentException("Collection's size and request count must be the same");
		return IntStream.range(0, requestCount).mapToObj(
				i -> invokeTargetAsync(method, configureTarget(path, header), collectionEntity.get(i), responseType))
				.collect(Collectors.toList());
	}

}
