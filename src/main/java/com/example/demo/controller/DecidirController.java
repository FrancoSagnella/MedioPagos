package com.example.demo.controller;

import java.net.URI;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.example.demo.clasesDecidir.ItemDecidir;
import com.example.demo.clasesDecidir.MedioPago;
import com.example.demo.clasesDecidir.RespuestaPaymentDecidir;
import com.example.demo.clasesDecidir.RespuestaTokenDecidir;
import com.example.demo.clasesDecidir.SolicitudDecidir;
import com.example.demo.clasesMercadoPago.RespuestaLoca;
import com.example.demo.entities.Pago;
import com.example.demo.entities.Transaccion;
import com.example.demo.service.PagoService;
import com.example.demo.service.TransaccionService;

@RestController
@RequestMapping(value = "/api/pagos/decidir")
public class DecidirController {

	@Autowired
	private PagoService pagoService;
	
	@Autowired
	private TransaccionService transaccionService;
	
	@CrossOrigin(origins = "*")
	@PostMapping(value = "/token/{id}")
	public ResponseEntity<?> createToken (@PathVariable(value = "id") Long pagoId, @RequestBody SolicitudDecidir resumen){
		
//		BUSCO PAGO ASOCIADO EN LA BBDD
		Optional<Pago> oPago = pagoService.findById(pagoId);
				
		try {
//			GENERO TOKEN
			RestTemplate rest = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.set("apikey", "4ae76f00234843d1af5994ed4674fd76");
			HttpEntity<SolicitudDecidir> entity = new HttpEntity<>(resumen, headers);
			ResponseEntity<RespuestaTokenDecidir> resToken = rest.postForEntity("https://developers.decidir.com/api/v2/tokens", entity, RespuestaTokenDecidir.class);
			
			System.out.println(resToken.getBody().id);
			
//			ARMO EL ITEM DE DECIDIR
			ItemDecidir item = new ItemDecidir();
				item.payment_method_id = resumen.medio_pago;
				//RECORTO LOS PRIMEROS 6 NUMEROS DE LA TARJETA
				item.bin = resumen.card_number.substring(0, 6);
				item.amount = oPago.get().getPrecioTotal();
				item.currency = "ARS";
				item.installments = (long) 1;
				item.description = "";
				item.payment_type = "single";
				item.sub_payments = new String[0];
			
//			EJECUTO PAGO
			return this.executePayment(resToken.getBody(), item, oPago.get());

			
			
			
//		SE MANEJAN LOS ERRORES DE AUTORIZACION AL CREAR TOKEN
		}catch(HttpClientErrorException e)
		{
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getResponseBodyAsByteArray());
		}
		catch(Exception e)
		{
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	
	
	
	public ResponseEntity<?> executePayment (RespuestaTokenDecidir token, ItemDecidir item, Pago pago){
	
		try {
//			EJECUTO PAGO
			RestTemplate rest = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.set("apikey", "3891f691dc4f40b6941a25a68d17c7f4");
			
//			AL ITEM A PAGAR LE AGREGO EL TOKEN Y SITE TRANSACTION
			item.token = token.id;
			item.site_transaction_id = "decidirPago-"+pago.getId()+"-"+new Date().getTime();
//			item.site_transaction_id = "decidirPago-"+pago.getId();
			
//			EJECUTO PAGO
			HttpEntity<ItemDecidir> entity = new HttpEntity<>(item, headers);
			ResponseEntity<RespuestaPaymentDecidir> resPayment = rest.postForEntity("https://developers.decidir.com/api/v2/payments", entity, RespuestaPaymentDecidir.class);

//			CREO TRANSACCION Y ACTUALIZO PAGO
			Transaccion transaccion = new Transaccion();
			transaccion.setIdMedioPago((long) 2);
			transaccion.setIdPago(pago.getId());
			transaccion.setIdTransaccion(""+resPayment.getBody().id);
			transaccion.setEstado(resPayment.getBody().status);
			transaccion.setFechaEstado(new Date().getTime());
			transaccionService.save(transaccion);
			
			pago.setEstadoPago(transaccion.getEstado());
			pago.setFechaEstado(transaccion.getFechaEstado());
			pagoService.save(pago);
			
//			SI EL ESTADO DEL PAGO ES APROBADO, SE NOTIFICA EL PAGO, SI ES RECHAZADO SE VA A RETORNAR EL ERROR PARA REINTENTAR
			if(resPayment.getBody().status.equals("approved"))
			{
				pago.setNotificado(true);
				pagoService.save(pago);
				
				URI uri = new URI(pago.getNotificationUrl());	
				
				RespuestaLoca res = new RespuestaLoca();
				res.estado = pago.getEstadoPago();
				res.idTransaccionConsumidor = pago.getIdTransaccionConsumidor();
				
				ResponseEntity<String> response = rest.postForEntity(uri, res, String.class);
				System.out.println(response.getBody());

				//RETORNA TODO PIOLA
				return ResponseEntity.status(HttpStatus.ACCEPTED).body(resPayment.getBody());
			}
//			SE MANEJAN PAGOS RECHAZADOS, EL PAGO SE ACTUALIZO EN LA BASE, Y SE CREO TRANSACCION, PERO NO SE NOTIFICA
			else
			{
				//RETORNA QUE HUBO UN ERROR
				return ResponseEntity.status(HttpStatus.CONFLICT).body(resPayment.getBody());
			}
			
			
			
			
//		SE MANEJARIAN ERRORES HACIA EL CREAR PAGO DE DECIDIR
		}catch(HttpClientErrorException e)
		{
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getResponseBodyAsByteArray());
		}
		catch(Exception e)
		{
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	
	
	@CrossOrigin(origins = "*")
	@GetMapping(value = "/payment-method/{id}")
	public ResponseEntity<?> getMedios(@PathVariable(value = "id") Long medioId){
		
		RestTemplate rest = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("apikey", "4ae76f00234843d1af5994ed4674fd76");

		HttpEntity<Void> entity = new HttpEntity<>(headers);
		ResponseEntity<MedioPago[]> resPayment = rest.exchange("https://developers.decidir.com/api/v2/payment-methods/1", HttpMethod.GET, entity, MedioPago[].class);

		return ResponseEntity.status(HttpStatus.ACCEPTED).body(resPayment.getBody());
	}
}
