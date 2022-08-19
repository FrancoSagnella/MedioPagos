package com.example.demo.controller;

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
}
