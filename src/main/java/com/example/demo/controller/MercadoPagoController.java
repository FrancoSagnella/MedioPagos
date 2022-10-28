package com.example.demo.controller;

import java.net.URI;
import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.clasesMercadoPago.Item;
import com.example.demo.clasesMercadoPago.NotificacionMP;
import com.example.demo.clasesMercadoPago.RespuestaLoca;
import com.example.demo.clasesMercadoPago.RespuestaLocaMP;
import com.example.demo.entities.Pago;
import com.example.demo.entities.Transaccion;
import com.example.demo.middlewares.MiddlewareException;
import com.example.demo.middlewares.Middlewares;
import com.example.demo.service.PagoService;
import com.example.demo.service.TransaccionService;
import com.mercadopago.client.payment.PaymentRefundClient;
import com.mercadopago.client.preference.PreferenceBackUrlsRequest;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferencePaymentMethodsRequest;
import com.mercadopago.client.preference.PreferencePaymentTypeRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.payment.PaymentRefund;
import com.mercadopago.resources.preference.Preference;

@RestController
@RequestMapping("/api/pagos/MercadoPago")
public class MercadoPagoController {

	private Middlewares middleware = new Middlewares();
	
	@Autowired
	private PagoService pagoService;
	
	@Autowired
	private TransaccionService transaccionService;
	
	
//	MERCADOPAGO
	
	//ESTE ES AL QUE LE MANDO DESDE EL FRONT, ES EL QUE ME HACE FUNCAR MERCADO PAGO
	@CrossOrigin(origins = "*")
	@PostMapping(value = "/creates/{id}")
	public ResponseEntity<?> create(@PathVariable(value = "id") Long pago_id, @RequestBody Item item) throws MPException, MPApiException{
		
//		BUSCO PAGO ASOCIADO EN LA BBDD
		Optional<Pago> oPago = pagoService.findById(pago_id);
		
		//MIDDLEWARE VALIDA VENCIMIENTO
		try {
			middleware.verificarVencimiento(oPago.get());
			middleware.verificarNotificado(oPago.get());
		}
		catch(MiddlewareException e)
		{
			return ResponseEntity.status(HttpStatus.CONFLICT).body(e.customError());
		}
		
		// Directamente creo la preferencia y demas
		Preference preference = new Preference();
		PreferenceClient client = new PreferenceClient();

		// Crea un ítem en la preferencia
		List<PreferenceItemRequest> items = new ArrayList<>();	 
		
		PreferenceItemRequest item1 = PreferenceItemRequest.builder()			   
				   .title(item.getTitle())				  
				   .quantity(item.getQuantity())			   
				   .unitPrice(item.getUnitPrice())
				   .categoryId(item.getCategoryId())
				   .currencyId(item.getCurrencyId())
				   .description(item.getDescription())
				   .pictureUrl(item.getPictureUrl())
				   .build();
				
		items.add(item1);
		
		PreferenceBackUrlsRequest backUrls = PreferenceBackUrlsRequest
				   .builder()
			       .success("medio-pagos.herokuapp.com/api/pagos/MercadoPago/respuesta_mercadoPago/"+pago_id)
			       .pending("medio-pagos.herokuapp.com/api/pagos/MercadoPago/respuesta_mercadoPago/"+pago_id)
			       .failure("medio-pagos.herokuapp.com/api/pagos/MercadoPago/respuesta_mercadoPago/"+pago_id)
			       .build();
		
		OffsetDateTime dateTimeNow = OffsetDateTime.now();	
		
		//LE PONGO DE VENCIMIENTO A LA PREFERENCIA, EL MISMO VENCIMIENTO QUE MI PAGO
		OffsetDateTime dateTimeTo = OffsetDateTime.ofInstant(new Timestamp(oPago.get().getFechaVencimiento()).toInstant(), ZoneId.of("-3"));
		
		
		List<PreferencePaymentTypeRequest> excludedPaymentTypes = new ArrayList<>();
		excludedPaymentTypes.add(PreferencePaymentTypeRequest.builder().id("ticket").build());
		
		//La ORTRA ES ACTUALIZO EL VENCIMIENTO DE MI PAGO, CON EL VENCIMIENTO QUE LE PUSE EN MP
//		OffsetDateTime dateTimeTo = dateTimeNow.plusMinutes(1);
//		
//		oPago.get().setFechaVencimiento(Timestamp.from(dateTimeTo.toInstant()).getTime());
//		pagoService.save(oPago.get());
		
//		System.out.println("offset: "+dateTimeTo);
//		System.out.println("timestamp: "+oPago.get().getFechaVencimiento());
		
		
		try {
			//Con el binaryMode, solo recibo aprobado o rechazado, no me puede quedar pendiente			
			PreferenceRequest request = PreferenceRequest.builder()
					.items(items)
					.backUrls(backUrls)
					.autoReturn("approved")
					.binaryMode(true)
					.notificationUrl("https://medio-pagos.herokuapp.com/api/pagos/MercadoPago/notificacion_mercadoPago/"+pago_id)
					.expires(true)
					.expirationDateFrom(dateTimeNow)
					.expirationDateTo(dateTimeTo)
					.externalReference(""+pago_id)
					.paymentMethods( PreferencePaymentMethodsRequest.builder().excludedPaymentTypes(excludedPaymentTypes).installments(1).build() )
					.build();
			preference =client.create(request);
			return new ResponseEntity<>(preference,HttpStatus.OK);
			
		} catch (MPApiException e) {
			System.out.println(e.getMessage());
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.OK);	
		}catch (MPException e) {
			System.out.println("TIRA MPEXCEPTION");
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.OK);	
		}	
	}
	
//	NOTIFICATION DE MERCADOPAGO, ACA RECIBO LA RESPUESTA SOBRE EL ESTADO DE UN PAGO
//	Aca deberia: ir a buscar a mercadoPago toda la data del pago, una vez la recupero, mando el post con la notificacion al notification url que me pasen de SIE
	@CrossOrigin(origins = "*")
	@PostMapping(value = "/notificacion_mercadoPago/{id}")
	public String respuestaLoca(@PathVariable(value="id") Long pago_id, @RequestBody NotificacionMP param1) {
		
//		return new ModelAndView("redirect:"+"https://www.google.com.ar/");
		
		RestTemplate rest = new RestTemplate();
		Pago pago = pagoService.findById(pago_id).get();
		ResponseEntity<RespuestaLocaMP> responseMP;
		ResponseEntity<String> response;
		
		try {
			middleware.verificarNotificado(pago);
		}
		catch(MiddlewareException e)
		{
			return "Ya fue notificado";
		}
//		
//		Voy a buscar a mercado pago el pago que me llego en la notificacion
		try {
			URI uriMP = new URI("https://api.mercadopago.com/v1/payments/"+param1.getData().getId()+"?access_token=TEST-1016222742358593-062310-6eaddcc1b5893e037fa1281c4a6abe16-683211147");	
				
			responseMP = rest.getForEntity(uriMP, RespuestaLocaMP.class);

			//Cuando recibo la respuesta de mercado pago primero creo una transacción
			//Se hizo una transaccion, con el estado que haya resultado		
			Date date = new Date();
			Transaccion trans = new Transaccion();
			
			trans.setIdPago(pago_id);
			trans.setEstado(responseMP.getBody().status);
			trans.setFechaEstado(date.getTime());
			trans.setIdTransaccion(responseMP.getBody().id);
			trans.setIdMedioPago((long) 1);
			transaccionService.save(trans);
			
			//Actualizo el estado del pago tambien (con el mismo estado que me trajo la transaccion, asi despues, cuando se ejecute el cron, lo va a poder notificar)
			pago.setEstadoPago(trans.getEstado());				
			pago.setFechaEstado(date.getTime());
			pagoService.save(pago);

			
//			Solo voy ba notificar si el pago esta aprobado (porque si fue rechazado MP me va a dejar intentar de nuevo,
//			o puede salir de MP, pero intentar de nuevo desde mi portal, entonces no puedo notificar muchos pagos rechazados,
//			tengo que estar seguro de que ese rechazo es definitivo, por eso la notificacion del rechazo se va a hacer tocando el boton cancelar en el portal,
//			o con el cron)
			if(pago.getEstadoPago().equals("approved"))
			{		
				
				pago.setNotificado(true);
				pagoService.save(pago);
				
				//Despues de actualizar el pago en mi base, mando la respuesta al cliente (con el notification url del pago)		
				URI uri = new URI(pago.getNotificationUrl());	
				
				//En la respuesta le envio el estado de la transaccion, y el id suyo de la transaccion, que yo relacione con el pago
				RespuestaLoca res = new RespuestaLoca();
				res.estado = trans.getEstado();
				res.idTransaccionConsumidor = pago.getIdTransaccionAplicacion();
				
				response = rest.postForEntity(uri, res, String.class);
				System.out.println(response.getBody());
				
			}
			
			
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
		
		return "a";
	}
	
	
//	ACA RECIBO RESPUESTA DE MERCADO PAGO, PERO EL REALIDAD TODO LO REFERENTE A NOTIFICAR EL APGO Y DEMAS YA LO HICE,
//	ACA ESTOY LLEGANDO SOLAMENTE CUANDO SE HACE EL AUTORETURN O SE TOCA EL BOTON VOLVER EN MERCADO PAGO, LO UNICO QUE HACE ES REDIRIGIRME
//	A LA PAGINA FINAL DE MI APP
	@CrossOrigin(origins = "*")
	@GetMapping(value = "/respuesta_mercadoPago/{id}")
	public ModelAndView redirect(@PathVariable(value = "id") Long pago_id, @RequestParam(value="collection_id") Long collection_id, @RequestParam(value="collection_status") String collection_status, 
							@RequestParam(value="payment_id") Long payment_id, @RequestParam(value="status") String status,
							@RequestParam(value="external_reference") String external_reference, @RequestParam(value="payment_type") String payment_type,
							@RequestParam(value="merchant_order_id") Long merchant_order_id, @RequestParam(value="preference_id") String preference_id,
							@RequestParam(value="site_id") String site_id, @RequestParam(value="processing_mode") String processing_mode,
							@RequestParam(value="merchant_account_id") String merchant_account_id) {

		//Aca ya se actualizo mi base y se mando respuesta al cliente, redirijo a mi app a la pantalla de resultado
		if(status.equals("approved")){
			return new ModelAndView("redirect:"+System.getProperty("frontUrl")+"confirmacion/aprobado/"+pago_id+"/0");			
		}
		else {
			return new ModelAndView("redirect:"+System.getProperty("frontUrl")+"principal/"+pago_id);
		}

	}
	
	
//	ESTO EMULA EL BACK EXTERNO AL QUE YO LE MANDO EL RETORNO
	@CrossOrigin(origins = "*")
	@PostMapping(value = "/respuesta")
	public String respuestaLoca(@RequestBody RespuestaLoca param1) {
		return "retorno del consumidor al recibir la info que le mande, le llego piola";
	}
	
	
	
	//Devoluciones
	public static ResponseEntity<?> reembolso (Long pago_id) {

        
		PaymentRefundClient client = new PaymentRefundClient();
		PaymentRefund respuesta;
		
		try {
			
			respuesta = client.refund(pago_id);
			
			System.out.println(respuesta.getStatus().toString());
			System.out.println(respuesta.getId().toString());
			System.out.println(respuesta.getPaymentId().toString());
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
		
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(respuesta);
		
	}
	
	
	
	
	
	
	
	@CrossOrigin(origins = "*")
	@PostMapping(value = "/process_payment")
	public ResponseEntity<?> pagoAPI(@RequestBody RespuestaLoca param)
	{
		return new ResponseEntity<String>("Retorno", HttpStatus.OK);	
	}
	
}
