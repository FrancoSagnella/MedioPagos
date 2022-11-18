package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.mercadopago.MercadoPagoConfig;


@SpringBootApplication
@Configuration
@EnableScheduling
public class MedioPagoClienteApplication {
	
	public static void main(String[] args) {
		
		SpringApplication.run(MedioPagoClienteApplication.class, args);
//		MercadoPagoConfig.setAccessToken("TEST-7629534519570845-072515-b85ce1449267138aa659cbf2a59bf991-306891134");//MIA
	}
	
	@Autowired
    void loadCert(@Value("${mercadopago.token}") String tokenMP) {
        MercadoPagoConfig.setAccessToken(tokenMP);
    }
	
}