package com.example.demo.cronJob;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandler;
import java.time.Month;
import java.time.Year;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.example.demo.clasesDecidir.RespuestaTokenDecidir;
import com.example.demo.clasesDecidir.SolicitudDecidir;
import com.example.demo.entities.ConciliacionMP;
import com.example.demo.entities.ConciliacionPrisma;
import com.example.demo.repository.ConciliacionMPRepository;
import com.example.demo.repository.ConciliacionPrismaRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
public class CronConciliacion {
	/*
	@Autowired
	private ConciliacionMPRepository mpRepo;
	
	@Autowired
	private ConciliacionPrismaRepository prismaRepo;
	
	//Una vez por dia, descarga los reportes de liquidacion de MP pago, relaciona los reportes descargados con sus transacciones correspondientes y se registran en la bbdd
	@Scheduled(fixedDelay = 10000)	
	public void guardarConciliacionMP() throws JsonMappingException, JsonProcessingException {
		
		
		
	}
	
	//Una vez por dia, descarga los reportes de liquidacion de Prisma, relaciona los reportes descargados con sus transacciones correspondientes y se registran en la bbdd
	@Scheduled(fixedDelay = 10000)	
	public void guardarConciliacionPrisma() throws JsonMappingException, JsonProcessingException {
		
		//Login en prisma
		String token = this.loginPrisma("asdasd", "asdasdasd");
		
		//agarro fehca de hoy y fecha de ayer
		Date date = new Date();
		int year = Year.now().getValue();	
		String fechaHasta = year+"-"+(date.getMonth()+1)+"-"+date.getDate();
		String fechaDesde = year+"-"+(date.getMonth()+1)+"-"+(date.getDate()-1);

		
		try {
			//Ver como pasarle bearer token
			RestTemplate rest = new RestTemplate();
			ResponseEntity<ConciliacionPrisma> res = rest.getForEntity("\"https://liquidacion.centralpos.com/api/v1/visa/movimientos?fpag-max=\"+fechaHasta+\"&fpag-min=\"+fechaDesde", ConciliacionPrisma.class);
		   

          } catch (Exception e) {
            e.printStackTrace();
          }  
		
		this.logoutPrisma();
		
	}
	
	
	
	
	//Tengo que ver como hacer las peticiones con bearer token
	private String loginPrisma(String email, String password) {
		return "asadasd";
	}
	
	private void logoutPrisma() {
		
	}*/
	
}
