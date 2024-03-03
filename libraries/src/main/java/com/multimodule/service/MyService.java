package com.multimodule.service;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Service
@Configuration
@EnableConfigurationProperties(ServiceProperties.class)
public class MyService {

	@Autowired
	private ServiceProperties serviceProperties;

	public MyService(ServiceProperties serviceProperties) {
		this.serviceProperties = serviceProperties;
	}

	public String message() {
		return this.serviceProperties.getMessage();
	}
}
