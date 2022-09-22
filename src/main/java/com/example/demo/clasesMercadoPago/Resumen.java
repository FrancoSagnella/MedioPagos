package com.example.demo.clasesMercadoPago;

import java.util.List;

import com.example.demo.entities.Aplicacion;
import com.example.demo.entities.Pagador;
import com.example.demo.entities.Pago;
import com.example.demo.entities.Producto;

public class Resumen {
	
	private Pago pago;
	private List<Producto> producto;
	private Aplicacion consumidor;
	private Pagador pagador;
	
	public Pago getPago() {
		return pago;
	}
	public void setPago(Pago pago) {
		this.pago = pago;
	}
	public List<Producto> getProducto() {
		return producto;
	}
	public void setProducto(List<Producto> producto) {
		this.producto = producto;
	}
	public Aplicacion getConsumidor() {
		return consumidor;
	}
	public void setConsumidor(Aplicacion consumidor) {
		this.consumidor = consumidor;
	}
	public Pagador getPagador() {
		return pagador;
	}
	public void setPagador(Pagador pagador) {
		this.pagador = pagador;
	}
}
