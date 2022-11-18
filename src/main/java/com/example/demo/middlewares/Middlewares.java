package com.example.demo.middlewares;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.demo.entities.Aplicacion;
import com.example.demo.entities.Pago;
import com.example.demo.service.AplicacionService;
import com.example.demo.service.PagoService;

public class Middlewares {
	
	@Autowired
	private PagoService pagoService;
	
	@Autowired 
	private AplicacionService aplicacionService;
	
	public boolean verificarVencimiento(Pago pago)
	{
		if( pago.getFechaVencimiento() < new Date().getTime() )
		{
			throw new MiddlewareException("vencimiento", "El pago esta vencido");
		}
		
		return true;
	}
	
	public boolean verificarNotificado(Pago pago)
	{
		if(pago.getNotificado()) {
			throw new MiddlewareException("notificado", "El pago ya fue notificado con estado"+pago.getEstadoPago());
		}
		
		return true;
	}
	
	public boolean verificarTokenAplicacion(Pago pago)
	{
		Optional<Aplicacion> oApp = aplicacionService.findByToken(pago.getIdAplicacion());
		if(!oApp.isEmpty()) {
			throw new MiddlewareException("notificado", "El pago ya fue notificado con estado"+pago.getEstadoPago());
		}
		
		return true;
	}
	
	public boolean verificarCarrito(Optional<Pago> oPago)
	{
		if(!oPago.isEmpty()){
			if(oPago.get().getEstadoPago().equals("pendiente") || oPago.get().getEstadoPago().equals("rejected")) {
				return false;
			}
			else {
				throw new MiddlewareException("carrito existente", "El idTransaccionAplicacion ya existe para un pago cuyo proceso terminó");				
			}
		}
		return true;
	}
}
