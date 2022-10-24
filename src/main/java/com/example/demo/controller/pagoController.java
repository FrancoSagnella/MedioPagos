package com.example.demo.controller;

import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.example.demo.clasesDecidir.RequestDevolucionDecidir;
import com.example.demo.clasesMercadoPago.RespuestaLoca;
import com.example.demo.clasesMercadoPago.Resumen;
import com.example.demo.entities.Aplicacion;
import com.example.demo.entities.Pagador;
import com.example.demo.entities.Pago;
import com.example.demo.entities.Producto;
import com.example.demo.entities.Transaccion;
import com.example.demo.middlewares.MiddlewareException;
import com.example.demo.middlewares.Middlewares;
import com.example.demo.service.AplicacionService;
import com.example.demo.service.PagadorService;
import com.example.demo.service.PagoService;
import com.example.demo.service.ProductoService;
import com.example.demo.service.TransaccionService;

@RestController
@RequestMapping("/api/pagos")
public class pagoController {
	
	private Middlewares middleware = new Middlewares();
	
	@Autowired
	private PagoService pagoService;
	
	@Autowired
	private ProductoService productoService;
	
	@Autowired
	private AplicacionService consumidorService;
	
	@Autowired
	private PagadorService pagadorService;
	
	@Autowired
	private TransaccionService transaccionService;
	
	// Create a new pago
	@CrossOrigin(origins = "*")
	@PostMapping
	public ResponseEntity<?> create (@RequestBody Resumen resumen){
		
//		TODO Validar el token del consumidor
//		VALIDAR SI EL PAGO YA EXISTE, SIMPLEMENTE DEVUELVO LA URL, SI NO EXISTE, LE HAGO EL ALTA
		
		resumen.getPago().setEstadoPago("pendiente");
		resumen.getPago().setNotificado(false);
		Date date = new Date();
		resumen.getPago().setFechaCreacion(date.getTime());
		resumen.getPago().setFechaEstado(date.getTime());
		Pagador pagador = pagadorService.save(resumen.getPagador());
		resumen.getPago().setIdPagador(pagador.getId());
		resumen.getPago().setFechaVencimiento(date.getTime()+10*60*1000);
		
		System.out.println(resumen.getPago().getIdAplicacion());
		
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
		myMap.put("url", System.getProperty("frontUrl")+pago.getId());
		
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

		//MIDDLEWARE VALIDA VENCIMIENTO, SI YA FUE NOTIFICADO O NO
		try {
			middleware.verificarVencimiento(oPago.get());
			middleware.verificarNotificado(oPago.get());
		}catch(MiddlewareException e)
		{
			return ResponseEntity.status(HttpStatus.CONFLICT).body(e.customError());
		}

		
		Optional<Aplicacion> oConsumidor = consumidorService.findByToken(oPago.get().getIdAplicacion());
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
	public ResponseEntity<?> create (@RequestBody Aplicacion resumen){
		
		Aplicacion pago = consumidorService.save(resumen);
		return ResponseEntity.status(HttpStatus.CREATED).body(pago);
	}
	
//	CANCELAR PAGO: ACA ME LLEGA DESDE MI APP, SI EN LA PANTALLA PRINCIPAL TOCAN CANCELAR
//	EN ESTE PUNTO TENGO QUE CANCELAR EL PAGO, Y NOTIFICAR AL NOTIFICATION URL DEL CONSUMIDOR Y REDIRIGIR AL BACK URL DEL CONSUMIDOR
	@CrossOrigin(origins = "*")
	@GetMapping(value = "/cancelar_pago/{id}")
	public ResponseEntity<?> cancelar(@PathVariable(value = "id") Long pago_id) {

//		Actualizo pago con CANCELADO
		Date date = new Date();
		Pago pago = pagoService.findById(pago_id).get();
		
		try {
			//MIDDLEWARE VALIDA VENCIMIENTO TIRA MIDDLEWAREEXCEPTION
			middleware.verificarVencimiento(pago);
			middleware.verificarNotificado(pago);

			
			pago.setEstadoPago("cancelado");				
			pago.setFechaEstado(date.getTime());
			pago.setNotificado(true);
			pagoService.save(pago);
			
	//		Notifico al cliente pago cancelado
			RestTemplate rest = new RestTemplate();
			ResponseEntity<String> response;
			//Despues de actualizar el pago en mi base, mando la respuesta al cliente (con el notification url del pago)		
			URI uri = new URI(pago.getNotificationUrl());	
			
			//En la respuesta le envio el estado de la transaccion, y el id suyo de la transaccion, que yo relacione con el pago
			RespuestaLoca res = new RespuestaLoca();
			res.estado = pago.getEstadoPago();
			res.idTransaccionConsumidor = pago.getIdTransaccionAplicacion();
			
			response = rest.postForEntity(uri, res, String.class);
			System.out.println(response.getBody());
		}
		catch(MiddlewareException e)
		{
			return ResponseEntity.status(HttpStatus.CONFLICT).body(e.customError());
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
		
		//Aca ya se actualizo mi base y se mando respuesta al cliente, redirijo al back url del pago
//		return new ModelAndView("redirect:"+pago.getBackUrl());

		Map<String, String> myMap = new HashMap<>();
		myMap.put("url", pago.getBackUrl());
		
		return ResponseEntity.status(HttpStatus.CREATED).body(myMap);
	}
	
	
	@CrossOrigin(origins = "*")
	@GetMapping(value = "/confirmar_pago/{id}")
	public ResponseEntity<?> confirmar(@PathVariable(value = "id") Long pago_id) {

		Pago pago = pagoService.findById(pago_id).get();
		
		Map<String, String> myMap = new HashMap<>();
		myMap.put("url", pago.getBackUrl());
		
		return ResponseEntity.status(HttpStatus.CREATED).body(myMap);
	}
	
	
	
	
	
	
	// DEVOLUCIONES
	//ENDPOINT PARA QUE CONSUMA SIE U OTRA APP
	//EN REALIDAD ESTO NO TENDRIA QUE ESTAR ACA, ESTO TENDRIA QUE ESTAR EN EL CONTROLADOR PRINCIPAL
	//QUE SE BUSQUE LA ULTIMA TRANSACCION (LA APROBADA) Y DEPENDIENDO SI ES DE MP O DE DECIDIR VA AL CORRESPONDIENTE CONTROLADOR.
	@CrossOrigin(origins = "*")
	@PostMapping(value = "/devolucion")
	public ResponseEntity<?> devolucionPago(@RequestBody RequestDevolucionDecidir request){
		
		//HABRIA QUE AGREGAR TODAS LAS VALIDACIONES NECESARIAS
		//1- SI ESTA APROBADO/NOTIFICADO
		//2- SI EXISTE EL TOKEN
		//3- SI EXISTE EL PAGO CON ESTE ID TRANSACCION
		//4- SI EL PAGO TIENE TRANSACCIONES
		//5- DE TENER TRANSACCIONES, SI TIENE UNA APROBADA
		//6- QUE NO TENGA MAS DE UNA APROBADA
		//7- VER QUE LA ULTIMA TRANSACCION SEA REALMENTE LA APROBADA (ENTIENDO QUE NO HAY CHANCES DE QUE PASE)
		//ETC.

		
		//Obtengo el pago asociado
		ArrayList<Pago> pagos = pagoService.findAllByIdAplicacion(request.tokenConsumidor);
		Pago miPago = new Pago();
		
		for(Pago pago : pagos)
		{
			Boolean a = pago.getIdTransaccionAplicacion().equals(request.idTransaccion);
			if(a)
			{
				miPago = pago;
				break;
			}
		}
	
		System.out.println(miPago.getId());
		
		//Con el pago ya identificado, obtengo la utima transaccion (la aprobada)
		Iterable<Transaccion> transacciones = transaccionService.findAllByIdPago(miPago.getId());
		Transaccion miTransaccion = new Transaccion();
		Long maxTimestamp = (long) 0;
		
		for(Transaccion transaccion : transacciones)
		{
			if(transaccion.getFechaEstado() > maxTimestamp)
			{
				maxTimestamp = transaccion.getFechaEstado();
				miTransaccion = transaccion;
			}
		}
		
		System.out.println(miTransaccion.getIdTransaccion());
		
		//Y ACA DEPENDIENDO EL MEDIO DE PAGO DE LA ULTIMA TRANSACCION, SE EJECUTA LA DEVOLUCION POR DECIDIR O POR MP
		ResponseEntity<?> res = ResponseEntity.status(HttpStatus.CONFLICT).body("a");
		switch(miTransaccion.getIdMedioPago().intValue())
		{
			//Mercado Pago
			case 1:
				res = MercadoPagoController.reembolso(miTransaccion.getIdTransaccion());
				break;
			//Decidir
			case 2:
				res = DecidirController.devolucionPago(miTransaccion.getIdTransaccion());
				break;
		}
		
		
		//Dependiendo del estatus de res, actualizo bbdd o devuelvo directamente error
		//Quizas habria que standarizar la respuesta que le doy a SIE, en vez de responder directamente lo que me responden los servicios de pago
		if(res.getStatusCode() == HttpStatus.ACCEPTED)
		{
//			CREO TRANSACCION Y ACTUALIZO PAGO
			Transaccion transaccion = new Transaccion();
			transaccion.setIdMedioPago(miTransaccion.getIdMedioPago());
			transaccion.setIdPago(miPago.getId());
			transaccion.setIdTransaccion(miTransaccion.getIdTransaccion());
			transaccion.setEstado("annuled");
			transaccion.setFechaEstado(new Date().getTime());
			transaccionService.save(transaccion);
			
			miPago.setEstadoPago("annuled");
			miPago.setFechaEstado(transaccion.getFechaEstado());
			pagoService.save(miPago);
		}

		return res;
	}
	
	@CrossOrigin(origins = "*")
	@GetMapping(value = "/getPago/{id}")
	public ResponseEntity<?> getDataPago(@PathVariable(value = "id") Long pago_id)
	{
		Map<String, Object> myMap = new HashMap<>();
		
		Pago pago = pagoService.findById(pago_id).get();
		myMap.put("pago", pago);
		
		Pagador pagador = pagadorService.findById(pago.getIdPagador()).get();
		myMap.put("pagador", pagador);
		
		ArrayList<Producto> productos = productoService.findAllByPago(pago_id);
		myMap.put("productos", productos);
		
		ArrayList<Transaccion> transacciones = transaccionService.findAllByIdPago(pago_id);
		myMap.put("transacciones", transacciones);
		
		Aplicacion consumidor = consumidorService.findByToken(pago.getIdAplicacion()).get(); 
		myMap.put("consumidor", consumidor);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(myMap);
	}

}
