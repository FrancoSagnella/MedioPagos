package com.example.demo.middlewares;

import java.util.Date;
import com.example.demo.entities.Pago;

public class Middlewares {
	
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
}
