package edu.cibertec.votoelectronico.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import edu.cibertec.votoelectronico.application.AppConfig;
import edu.cibertec.votoelectronico.resource.communication.ResumenProcesoResponse;

public class SimpleSSEVotoElectronicoClient {

	private static final Logger LOG = LoggerFactory.getLogger(SimpleSSEVotoElectronicoClient.class);

	private ApplicationContext ctx;
	private HttpClientWithSSE httpClient;

	public SimpleSSEVotoElectronicoClient() {
		this.ctx = new AnnotationConfigApplicationContext(AppConfig.class);
		this.httpClient = this.ctx.getBean(JAXRSAsyncHttpClientWithSSE.class);
	}

	public void listenForResultadoProcesoUpdate() {
		this.httpClient.listenOn("http://localhost:8080/v1/votoelectronico/subscribe/resultado", null, (response) -> {
			LOG.info(response.toString());
		}, ResumenProcesoResponse.class);
	}

	public static void main(String[] args) {
		try {
			SimpleSSEVotoElectronicoClient client = new SimpleSSEVotoElectronicoClient();
			client.listenForResultadoProcesoUpdate();
			LOG.info("Thread: [{}]", Thread.currentThread().getId());
			LOG.info("Thread: [{}] - Running task 1", Thread.currentThread().getId());
			LOG.info("Thread: [{}] - Running task 2", Thread.currentThread().getId());

			while (true) {
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
