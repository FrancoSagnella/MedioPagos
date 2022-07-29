package com.example.demo.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "transacciones")
public class Transaccion {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long idPago;
	private String estado;
	private Long fechaEstado;
	private String idTransaccion;
	private Long idMedioPago;
	
	public Long getIdPago() {
		return idPago;
	}
	public void setIdPago(Long id_pago) {
		this.idPago = id_pago;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public Long getFechaEstado() {
		return fechaEstado;
	}
	public void setFechaEstado(Long fecha_estado) {
		this.fechaEstado = fecha_estado;
	}
	public String getIdTransaccion() {
		return idTransaccion;
	}
	public void setIdTransaccion(String id_transaccion) {
		this.idTransaccion = id_transaccion;
	}
	public Long getIdMedioPago() {
		return idMedioPago;
	}
	public void setIdMedioPago(Long id_medio_pago) {
		this.idMedioPago = id_medio_pago;
	}
}
