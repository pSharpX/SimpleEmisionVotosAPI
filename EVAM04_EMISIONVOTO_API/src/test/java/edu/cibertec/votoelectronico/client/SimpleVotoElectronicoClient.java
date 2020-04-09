package edu.cibertec.votoelectronico.client;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import edu.cibertec.votoelectronico.application.PersistenceConfig;
import edu.cibertec.votoelectronico.dto.EmisionVotoDto;
import edu.cibertec.votoelectronico.resource.communication.EmisionVotoResponse;
import edu.cibertec.votoelectronico.resource.communication.ListVotoResponse;
import edu.cibertec.votoelectronico.resource.communication.ResumenProcesoResponse;

public class SimpleVotoElectronicoClient {

	private static final Logger LOG = LoggerFactory.getLogger(SimpleVotoElectronicoClient.class);

	private ApplicationContext ctx;
	private CommonAsyncHttpClient httpClient;

	public SimpleVotoElectronicoClient() {
		this.ctx = new AnnotationConfigApplicationContext(PersistenceConfig.class);
		this.httpClient = this.ctx.getBean(JAXRSAsyncHttpClient.class);
	}

	public void getResumenProcesoAsync() {
		CompletableFuture<ResumenProcesoResponse> promise = httpClient
				.getAsync("http://localhost:8080/v1/votoelectronico/resultado", null, ResumenProcesoResponse.class);
		LOG.info("Thread: [{}] - Promise has being called", Thread.currentThread().getId());

		promise.thenApplyAsync((ResumenProcesoResponse response) -> {
			LOG.info(response.toString());
			LOG.info("Thread: [{}] - Promise has being resolved", Thread.currentThread().getId());
			LOG.info("Thread: [{}]", Thread.currentThread().getId());
			return response;
		}).exceptionally((exception) -> {
			LOG.error("Thread: [{}] - Exception: [{}]", Thread.currentThread().getId(), exception);
			return new ResumenProcesoResponse(exception.getMessage());
		});
	}

	public void getListadoVotosAsync() {
		CompletableFuture<ListVotoResponse> promise = httpClient.getAsync("http://localhost:8080/v1/votoelectronico",
				null, ListVotoResponse.class);
		LOG.info("Thread: [{}] - Promise has being called", Thread.currentThread().getId());

		promise.thenApplyAsync((ListVotoResponse response) -> {
			LOG.info(response.toString());
			LOG.info("Thread: [{}] - Promise has being resolved", Thread.currentThread().getId());
			LOG.info("Thread: [{}]", Thread.currentThread().getId());
			return response;
		}).exceptionally((exception) -> {
			LOG.error("Thread: [{}] - Exception: [{}]", Thread.currentThread().getId(), exception);
			return new ListVotoResponse(exception.getMessage());
		});
	}

	public void createEmitirVotoAsync() {
		EmisionVotoDto data = new EmisionVotoDto();
//		data.setDni("48048360");
		data.setDni(String.format("48048%s", randomWithRange(100, 999)));
		data.setGrupoPolitico("P1");
		data.setFecha((new SimpleDateFormat("yyyyMMddHHmmss")).format(new Date()));
		CompletableFuture<EmisionVotoResponse> promise = httpClient
				.postAsync("http://localhost:8080/v1/votoelectronico/emitir", data, null, EmisionVotoResponse.class);
		LOG.info("Thread: [{}] - Promise has being called", Thread.currentThread().getId());

		promise.thenApplyAsync((EmisionVotoResponse response) -> {
			LOG.info(response.toString());
			LOG.info("Thread: [{}] - Promise has being resolved", Thread.currentThread().getId());
			LOG.info("Thread: [{}]", Thread.currentThread().getId());
			return response;
		}).exceptionally((exception) -> {
			LOG.error("Thread: [{}] - Exception: [{}]", Thread.currentThread().getId(), exception);
			return new EmisionVotoResponse(exception.getMessage());
		});
	}

	public void createCollectionEmitirVotoAsync() {
		List<EmisionVotoDto> payloads = this.createCollectionOfEmitirVoto();

		List<CompletableFuture<EmisionVotoResponse>> requestFutures = payloads.stream()
				.map(payload -> httpClient.postAsync("http://localhost:8080/v1/votoelectronico/async/emitir2", payload,
						null, EmisionVotoResponse.class))
				.collect(Collectors.toList());

		CompletableFuture<Void> allFutures = CompletableFuture
				.allOf(requestFutures.toArray(new CompletableFuture[requestFutures.size()]));

		@SuppressWarnings("unused")
		CompletableFuture<List<EmisionVotoResponse>> allRequestsFuture = allFutures.thenApply(v -> {
			return requestFutures.stream().map(requestFuture -> requestFuture.join()).collect(Collectors.toList());
		}).thenApply((List<EmisionVotoResponse> response) -> {
			LOG.info(response.toString());
			LOG.info("Thread: [{}] - Promise has being resolved", Thread.currentThread().getId());
			LOG.info("Thread: [{}]", Thread.currentThread().getId());
			return response;
		}).exceptionally((exception) -> {
			LOG.error("Thread: [{}] - Exception: [{}]", Thread.currentThread().getId(), exception);
			return Arrays.asList(new EmisionVotoResponse());
		});
	}

	public void createCollectionEmitirVotoAsync2() {
		List<EmisionVotoDto> payloads = this.createCollectionOfEmitirVoto();

		long start = System.nanoTime();
		List<CompletableFuture<EmisionVotoResponse>> requestFutures = httpClient.requestAsync(
				"http://localhost:8080/v1/votoelectronico/async/emitir2", REQUEST_METHOD.POST, payloads, null,
				EmisionVotoResponse.class, payloads.size());

		long created = requestFutures.stream().map(CompletableFuture::join).peek((r) -> LOG.info(r.toString()))
				.filter(r -> r.isSuccess()).count();
		long duration = (System.nanoTime() - start) / 1_000_000;
		LOG.info("Final Result - Total Created: [{}], in [{}]", created, duration);
	}
	
	public void createCollectionEmitirVotoAsync3() {
		List<EmisionVotoDto> payloads = this.createCollectionOfEmitirVoto();

		long start = System.nanoTime();
		List<CompletionStage<EmisionVotoResponse>> requestFutures = httpClient.requestNativeAsync(
				"http://localhost:8080/v1/votoelectronico/async/emitir2", REQUEST_METHOD.POST, payloads, null,
				EmisionVotoResponse.class, payloads.size());

//		long created = requestFutures.stream().reduce((l, r) -> l.thenCombine(r, (r1, r2) -> )).map(CompletableFuture::join).peek((r) -> LOG.info(r.toString()))
//				.filter(r -> r.isSuccess()).count();
		
//		long created = requestFutures.stream().filter(stage -> stage.thenApply(response -> )).map(CompletableFuture::join).peek((r) -> LOG.info(r.toString()))
//				.filter(r -> r.isSuccess()).count();
		
		
		long duration = (System.nanoTime() - start) / 1_000_000;
//		LOG.info("Final Result - Total Created: [{}], in [{}]", created, duration);
	}

	public void createEmitirVotoThenGetListadoVotosAsyncAndGetResumenProcesoAsync() {
		EmisionVotoDto data = new EmisionVotoDto();
		data.setDni(String.format("48048%s", randomWithRange(100, 999)));
		data.setGrupoPolitico("P1");
		data.setFecha((new SimpleDateFormat("yyyyMMddHHmmss")).format(new Date()));
		CompletableFuture<EmisionVotoResponse> promise = httpClient
				.postAsync("http://localhost:8080/v1/votoelectronico/emitir", data, null, EmisionVotoResponse.class)
				.thenApply((response) -> {
					LOG.info(response.toString());
					LOG.info("Thread: [{}] - Promise has being resolved", Thread.currentThread().getId());
					return response;
				});

		promise.thenRunAsync(() -> {
			httpClient.getAsync("http://localhost:8080/v1/votoelectronico", null, ListVotoResponse.class)
					.thenApply((response) -> {
						LOG.info(response.toString());
						LOG.info("Thread: [{}] - Promise has being resolved", Thread.currentThread().getId());
						return response;
					});
		});
		promise.thenRunAsync(() -> {
			httpClient
					.getAsync("http://localhost:8080/v1/votoelectronico/resultado", null, ResumenProcesoResponse.class)
					.thenApply((response) -> {
						LOG.info(response.toString());
						LOG.info("Thread: [{}] - Promise has being resolved", Thread.currentThread().getId());
						return response;
					});
		});

	}

	private List<EmisionVotoDto> createCollectionOfEmitirVoto() {
		List<EmisionVotoDto> collection = new ArrayList<EmisionVotoDto>();
		for (int i = 0; i < 50; i++) {
			EmisionVotoDto emisionVotoDto = new EmisionVotoDto();
			emisionVotoDto.setFecha((new SimpleDateFormat("yyyyMMddHHmmss")).format(new Date()));
			emisionVotoDto.setDni(String.format("48048%s", randomWithRange(100, 999)));
			emisionVotoDto.setGrupoPolitico((i % 2) == 0 ? "P1" : "P2");
			collection.add(emisionVotoDto);
		}
		return collection;
	}

	private int randomWithRange(int min, int max) {
		int range = (max - min) + 1;
		return (int) (Math.random() * range) + min;
	}

	public static void main(String[] args) {
		try {
			SimpleVotoElectronicoClient client = new SimpleVotoElectronicoClient();
//			client.getResumenProcesoAsync();
//			client.getListadoVotosAsync();
//			client.createEmitirVotoAsync();
//			client.createCollectionEmitirVotoAsync();
			client.createCollectionEmitirVotoAsync2();
//			client.createEmitirVotoThenGetListadoVotosAsyncAndGetResumenProcesoAsync();
			LOG.info("Thread: [{}]", Thread.currentThread().getId());
			LOG.info("Thread: [{}] - Running task 1", Thread.currentThread().getId());
			LOG.info("Thread: [{}] - Running task 2", Thread.currentThread().getId());
			LOG.info("Thread: [{}] - Running task 3", Thread.currentThread().getId());

			while (true) {
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
