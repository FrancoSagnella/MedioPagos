package com.example.demo.cronJob;

import java.util.ArrayList;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.example.demo.clasesMercadoPago.RespuestaLoca;
import com.example.demo.entities.Pago;
import com.example.demo.service.PagoService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
public class CronNotificarPago {
	
	@Autowired
	private PagoService pagoService;

//	CRON
	//se ejecuta cada 1 minuto desde que termino la ejecucion anterior
		@Scheduled(fixedDelay = 60000)	
		public void reportTime() throws JsonMappingException, JsonProcessingException {
			
			//SE OBTIENEN DE LA BASE DE DATOS TODOS LOS PAGOS QUE NO HAN SIDO NOTIFICADOS
			System.out.println("ejecutando revision de vencimiento"+ new Date());
			ArrayList<Pago> pagosNoNotificados = pagoService.findByNotificado(false);
			
			//SE CREA EL GET REQUEST A MERCADOPAGO PARA VER SI LA PREFERENCIA EXPIRO
			RestTemplate restTemplate = new RestTemplate();
//			String accessToken = "TEST-1016222742358593-062310-6eaddcc1b5893e037fa1281c4a6abe16-683211147";
			RespuestaLoca res = new RespuestaLoca();
			
			
			for (int i = 0; i < pagosNoNotificados.size(); i++) {
				
				//HASTA DONDE ENTIENDO, CON LO QUE SE ME OCURRIO DE HACER YO LA FECHA DE VENCIMIENTO SIRVE TANTO PARA DECIDIR COMO PARA MERCADOPAGO
				//Con los pagos notificados, me fijo si estan vencidos o no.
				if(pagosNoNotificados.get(i).getFechaVencimiento() < new Date().getTime())
				{
					//SI ESTA VENCIDO, LO NOTIFICO
					pagosNoNotificados.get(i).setNotificado(true);
					pagosNoNotificados.get(i).setEstadoPago("no procesado");
					
					String urlNotificacion = (pagosNoNotificados.get(i).getNotificationUrl());
					
					res.estado = pagosNoNotificados.get(i).getEstadoPago();
					res.idTransaccionConsumidor = pagosNoNotificados.get(i).getIdTransaccionConsumidor();
					
					pagoService.save(pagosNoNotificados.get(i));
					
					restTemplate.postForEntity(urlNotificacion, res, String.class);
				}
				
//				if (pagosNoNotificados.get(i).getIdPreferencia() != null) {		
//					
//					String urlPreferenciaMercadoPago = "https://api.mercadopago.com/checkout/preferences/"+pagosNoNotificados.get(i).getIdPreferencia()+"?access_token="+accessToken;
//					ResponseEntity<String> response = restTemplate.getForEntity(urlPreferenciaMercadoPago, String.class);
//					ObjectMapper mapper = new ObjectMapper();
//					JsonNode root = mapper.readTree(response.getBody());
//					JsonNode expirationDate = root.path("expiration_date_to");
//					String expriationDateToString = expirationDate.toString();
//					//LE QUITO LAS COMILLAS DOBLES ANTES Y DESPUES DEL STRING DE LA FECHA PORQUE SINO NO SE PUEDE COMPARAR
//					String result = expriationDateToString.replaceAll("^\"|\"$", "");
//					
//					
//					OffsetDateTime dateNow = OffsetDateTime.now();
//					System.out.println(dateNow.compareTo(OffsetDateTime.parse(result, DateTimeFormatter.ISO_DATE_TIME)));
//					
//					
//					if (dateNow.compareTo(OffsetDateTime.parse(result, DateTimeFormatter.ISO_DATE_TIME)) > 0) {
//						
//						pagosNoNotificados.get(i).setNotificado(true);
//						pagosNoNotificados.get(i).setEstadoPago("no procesado");
//						String urlNotificacion = (pagosNoNotificados.get(i).getNotificationUrl());
//						res.estado = pagosNoNotificados.get(i).getEstadoPago();
//						res.idTransaccionConsumidor = pagosNoNotificados.get(i).getIdTransaccionConsumidor();
//						pagoService.save(pagosNoNotificados.get(i));
//						restTemplate.postForEntity(urlNotificacion, res, String.class);
//									
//					}
//					
//					
//				}else {
//					
//					
//					pagosNoNotificados.get(i).setNotificado(true);
//					pagosNoNotificados.get(i).setEstadoPago("no procesado");
//					String urlNotificacion = (pagosNoNotificados.get(i).getNotificationUrl());
//					res.estado = pagosNoNotificados.get(i).getEstadoPago();
//					res.idTransaccionConsumidor = pagosNoNotificados.get(i).getIdTransaccionConsumidor();
//					pagoService.save(pagosNoNotificados.get(i));
//					restTemplate.postForEntity(urlNotificacion, res, String.class);
//					
//				}
				
				
				
			}
		}
}
