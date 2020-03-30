package edu.cibertec.votoelectronico.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
public class BeanUtil implements SpringContextBridgedServices, ApplicationContextAware {

	private static ApplicationContext context;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		BeanUtil.context = applicationContext;
	}

	public static SpringContextBridgedServices services() {
		return context.getBean(SpringContextBridgedServices.class);
	}

	@Override
	public <T> T getService(Class<T> serviceType) {
		return context.getBean(serviceType);
	}

}
