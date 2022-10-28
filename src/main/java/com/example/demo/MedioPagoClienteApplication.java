package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.mercadopago.MercadoPagoConfig;


@SpringBootApplication
@Configuration
@EnableScheduling
public class MedioPagoClienteApplication {

	public static void main(String[] args) {

//		CONFIGURO ENVIRONMENT PROPERTIES
		System.setProperty("frontUrl", "http://correo-app-frontend-correo-argentino.apps.lab.okd.local/");
		System.setProperty("backUrl", "http://correo-app-backend2-correo-argentino.apps.lab.okd.local/api/pagos/");


		SpringApplication.run(MedioPagoClienteApplication.class, args);
		MercadoPagoConfig.setAccessToken("TEST-1016222742358593-062310-6eaddcc1b5893e037fa1281c4a6abe16-683211147");//DE JULI
//		MercadoPagoConfig.setAccessToken("TEST-7629534519570845-072515-b85ce1449267138aa659cbf2a59bf991-306891134");//MIA
	}
	
}