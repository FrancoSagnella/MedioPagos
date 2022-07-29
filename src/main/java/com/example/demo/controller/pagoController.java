package com.example.demo.controller;

import java.net.URI;
import java.util.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

import com.example.demo.entities.Consumidor;
import com.example.demo.entities.Item;
import com.example.demo.entities.NotificacionMP;
import com.example.demo.entities.Pagador;
import com.example.demo.entities.Pago;
import com.example.demo.entities.Producto;
import com.example.demo.entities.RespuestaLoca;
import com.example.demo.entities.RespuestaLocaMP;
import com.example.demo.entities.Resumen;
import com.example.demo.entities.Transaccion;
import com.example.demo.service.ConsumidorService;
import com.example.demo.service.PagadorService;
import com.example.demo.service.PagoService;
import com.example.demo.service.ProductoService;
import com.example.demo.service.TransaccionService;
import com.mercadopago.client.preference.PreferenceBackUrlsRequest;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.preference.Preference;

@RestController
@RequestMapping("/api/pagos")
public class pagoController {
	
	@Autowired
	private PagoService pagoService;
	
	@Autowired
	private ProductoService productoService;
	
	@Autowired
	private ConsumidorService consumidorService;
	
	@Autowired
	private TransaccionService transaccionService;
	
	@Autowired
	private PagadorService pagadorService;
	
	// Create a new pago
	@CrossOrigin(origins = "*")
	@PostMapping
	public ResponseEntity<?> create (@RequestBody Resumen resumen){
		
//		TODO Validar el token del consumidor
//		VALIDAR SI EL PAGO YA EXISTE, SIMPLEMENTE DEVUELVO LA URL, SI NO EXISTE, LE HAGO EL ALTA
		
		resumen.getPago().setEstadoPago("noPagado");
		Date date = new Date();
		resumen.getPago().setFechaCreacion(date.getTime());
		resumen.getPago().setFechaEstado(date.getTime());
		Pagador pagador = pagadorService.save(resumen.getPagador());
		resumen.getPago().setIdPagador(pagador.getId());
		Pago pago = pagoService.save(resumen.getPago());
		
		int len = resumen.getProducto().size();
		for(int i = 0; i < len; i++)
		{
			Producto prod = resumen.getProducto().get(i);
			prod.setPago(pago.getId());
			resumen.getProducto().set(i, prod);
			productoService.save(prod);
		}

		//Basicamente un stdClass, para poder retornar un objeto metiendole asi medio dinamico los atributos, pa retornar solo lo que quiero
		//Porque sino no se me caste a json xd
		Map<String, String> myMap = new HashMap<>();
		myMap.put("url", "http://localhost:4200/principal/"+pago.getId());
		
		return ResponseEntity.status(HttpStatus.CREATED).body(myMap);
	}
	
	// Read a pago
	@CrossOrigin(origins = "*")
	@GetMapping("/{id}")
	public ResponseEntity<?> read (@PathVariable(value = "id") Long pagoId){
		Optional<Pago> oPago = pagoService.findById(pagoId);
		if(!oPago.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		
		Optional<Consumidor> oConsumidor = consumidorService.findByToken(oPago.get().getIdConsumidor());
		Iterable<Producto> oProdutcos = productoService.findAllByPago(oPago.get().getId());
		Optional<Pagador> oPagador = pagadorService.findById(oPago.get().getIdPagador());
		
		Resumen resumen = new Resumen();
		resumen.setPago(oPago.get());
		resumen.setConsumidor(oConsumidor.get());
		resumen.setProducto((List<Producto>) oProdutcos);
		resumen.setPagador(oPagador.get());
		
		return ResponseEntity.ok(resumen); 
	}
	
	@CrossOrigin(origins = "*")
	@PostMapping("/consumidor")
	public ResponseEntity<?> create (@RequestBody Consumidor resumen){
		
		Consumidor pago = consumidorService.save(resumen);
		return ResponseEntity.status(HttpStatus.CREATED).body(pago);
	}
	
	
	
	
	
	
	
	
	
	

	
	
	
	
	
//	MERCADOPAGO
	
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

		Pago pago = pagoService.findById(pago_id).get();

		//Aca ya se actualizo mi base y se mando respuesta al cliente, redirijo a mi app a la pantalla de resultado
		if(status.equals("approved")){
			return new ModelAndView("redirect:"+pago.getBackUrl());			
		}
		else {
			return new ModelAndView("redirect:"+pago.getBackUrl());
		}

	}
	
	
	
	
//	ESTO EMULA EL BACK EXTERNO AL QUE YO LE MANDO EL RETORNO
	@CrossOrigin(origins = "*")
	@PostMapping(value = "/respuesta")
	public String respuestaLoca(@RequestBody RespuestaLoca param1) {
		return "retorno del consumidor al recibir la info que le mande, le llego piola";
	}
	
	
//	NOTIFICATION DE MERCADOPAGO, ACA RECIBO LA RESPUESTA SOBRE EL ESTADO DE UN PAGO
//	Aca deberia: ir a buscar a mercadoPago toda la data del pago, una vez la recupero, mando el post con la notificacion al notification url que me pasen de SIE
	@CrossOrigin(origins = "*")
	@PostMapping(value = "/notificacion_mercadoPago/{id}")
	public ModelAndView respuestaLoca(@PathVariable(value="id") Long pago_id, @RequestBody NotificacionMP param1) {
		
		return new ModelAndView("redirect:"+"https://www.google.com.ar/");
		
//		RestTemplate rest = new RestTemplate();
//		Pago pago = pagoService.findById(pago_id).get();
//		ResponseEntity<RespuestaLocaMP> responseMP;
//		ResponseEntity<String> response;
//		
//		Voy a buscar a mercado pago el pago que me llego en la notificacion
//		try {
//			URI uriMP = new URI("https://api.mercadopago.com/v1/payments/"+param1.getData().getId()+"?access_token=TEST-1016222742358593-062310-6eaddcc1b5893e037fa1281c4a6abe16-683211147");	
//				
//			responseMP = rest.getForEntity(uriMP, RespuestaLocaMP.class);
//			System.out.println("Es la respuesta con el payment que me da la api de mercado pago, recibo bien el estado");
//			System.out.println(responseMP.getBody().status);
//			System.out.println(responseMP.getBody().status_detail);
//			System.out.println(responseMP.getBody().id);
//
//			//Cuando recibo la respuesta de mercado pago primero creo una transacción
//			//Se hizo una transaccion, con el estado que haya resultado		
//			Date date = new Date();
//			Transaccion trans = new Transaccion();
//			
//			trans.setIdPago(pago_id);
//			trans.setEstado(responseMP.getBody().status);
//			trans.setFechaEstado(date.getTime());
//			trans.setIdTransaccion(""+responseMP.getBody().status);
//			trans.setIdMedioPago((long) 1);
//			transaccionService.save(trans);
//			
//			Ademas me fijo, si el estado de la transaccion fue aprobado, lo pongo al pago como pagado tambien
//			if(responseMP.getBody().status.equals("approved"))
//			{
//				pago.setEstadoPago("pagado");				
//				pago.setFechaEstado(date.getTime());
//				pagoService.save(pago);
//			}
//
//		
//			//Despues de actualizar el pago en mi base, mando la respuesta al cliente (con el back url del pago)		
//			URI uri = new URI(pago.getNotificationUrl());	
//			
//			//En la respuesta le envio el estado de la transaccion, y el id suyo de la transaccion, que yo relacione con el pago
//			RespuestaLoca res = new RespuestaLoca();
//			res.estado = trans.getEstado();
//			res.idTransaccionConsumidor = pago.getIdTransaccionConsumidor();
//			
//			response = rest.postForEntity(uri, res, String.class);
//			System.out.println(response.getBody());
//			
//			
//		}catch(Exception e){
//			System.out.println(e.getMessage());
//		}
		
//		return "a";
	}
	
	
	
//ESTE ES AL QUE LE MANDO DESDE EL FRONT, ES EL QUE ME HACE FUNCAR MERCADO PAGO
	@CrossOrigin(origins = "*")
	@PostMapping(value = "/creates/{id}")
	public ResponseEntity<?> create(@PathVariable(value = "id") Long pago_id, @RequestBody Item item) throws MPException, MPApiException{
		
		//Ahora no recupero el pago ni le modifico nada, porque hasta que mercadopago no me mande respuesta, no se realizo ninguna transaccion
		//yo recien registro las transacciones una vez me responde y tengo constancia de que se realizo		
		
		
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
			       .success("medio-pagos.herokuapp.com/api/pagos/respuesta_mercadoPago/"+pago_id)
			       .pending("medio-pagos.herokuapp.com/api/pagos/respuesta_mercadoPago/"+pago_id)
			       .failure("medio-pagos.herokuapp.com/api/pagos/respuesta_mercadoPago/"+pago_id)
			       .build();
		

		
		try {
			//Con el binaryMode, solo recibo aprobado o rechazado, no me puede quedar pendiente			
			PreferenceRequest request = PreferenceRequest.builder().items(items).backUrls(backUrls).autoReturn("approved").binaryMode(true).notificationUrl("https://medio-pagos.herokuapp.com/api/pagos/notificacion_mercadoPago/"+pago_id).build();
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
	
	
}
