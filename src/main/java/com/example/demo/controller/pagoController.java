package com.example.demo.controller;

import java.net.URI;
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

import com.example.demo.clasesMercadoPago.RespuestaLoca;
import com.example.demo.clasesMercadoPago.Resumen;
import com.example.demo.entities.Consumidor;
import com.example.demo.entities.Pagador;
import com.example.demo.entities.Pago;
import com.example.demo.entities.Producto;
import com.example.demo.service.ConsumidorService;
import com.example.demo.service.PagadorService;
import com.example.demo.service.PagoService;
import com.example.demo.service.ProductoService;

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
	private PagadorService pagadorService;
	
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
	
//	CANCELAR PAGO: ACA ME LLEGA DESDE MI APP, SI EN LA PANTALLA PRINCIPAL TOCAN CANCELAR
//	EN ESTE PUNTO TENGO QUE CANCELAR EL PAGO, Y NOTIFICAR AL NOTIFICATION URL DEL CONSUMIDOR Y REDIRIGIR AL BACK URL DEL CONSUMIDOR
	@CrossOrigin(origins = "*")
	@GetMapping(value = "/cancelar_pago/{id}")
	public ResponseEntity<?> cancelar(@PathVariable(value = "id") Long pago_id) {

//		Actualizo pago con CANCELADO
		Date date = new Date();
		Pago pago = pagoService.findById(pago_id).get();
		pago.setEstadoPago("cancelado");				
		pago.setFechaEstado(date.getTime());
		pago.setNotificado(true);
		pagoService.save(pago);
		
//		Notifico al cliente pago cancelado
		try {
			RestTemplate rest = new RestTemplate();
			ResponseEntity<String> response;
			//Despues de actualizar el pago en mi base, mando la respuesta al cliente (con el notification url del pago)		
			URI uri = new URI(pago.getNotificationUrl());	
			
			//En la respuesta le envio el estado de la transaccion, y el id suyo de la transaccion, que yo relacione con el pago
			RespuestaLoca res = new RespuestaLoca();
			res.estado = pago.getEstadoPago();
			res.idTransaccionConsumidor = pago.getIdTransaccionConsumidor();
			
			response = rest.postForEntity(uri, res, String.class);
			System.out.println(response.getBody());
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
//		return new ModelAndView("redirect:"+pago.getBackUrl());
//		return new ResponseEntity<String>(pago.getBackUrl(), HttpStatus.OK);
		
		Map<String, String> myMap = new HashMap<>();
		myMap.put("url", pago.getBackUrl());
		
		return ResponseEntity.status(HttpStatus.CREATED).body(myMap);
	}
}
