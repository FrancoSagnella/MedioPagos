package com.example.demo.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "pagos")
public class Pago {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id; //1
	private String backUrl;//2
	private String estadoPago;//3
	private Long fechaCreacion;//4
	private Long fechaEstado;//5
	private String idConsumidor;//6
	private Long idPagador;//7
	private String idTransaccionConsumidor;//8
	private String notificationUrl;	//9
	private Long precioTotal;//10
	private Boolean notificado;//11
	private String idPreferencia;//12
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getIdConsumidor() {
		return idConsumidor;
	}
	public void setIdConsumidor(String idConsumidor) {
		this.idConsumidor = idConsumidor;
	}
	public String getIdTransaccionConsumidor() {
		return idTransaccionConsumidor;
	}
	public void setIdTransaccionConsumidor(String idTransaccionConsumidor) {
		this.idTransaccionConsumidor = idTransaccionConsumidor;
	}
	public Long getFechaCreacion() {
		return fechaCreacion;
	}
	public void setFechaCreacion(Long fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}
	public Long getPrecioTotal() {
		return precioTotal;
	}
	public void setPrecioTotal(Long precioTotal) {
		this.precioTotal = precioTotal;
	}
	public String getBackUrl() {
		return backUrl;
	}
	public void setBackUrl(String backUrl) {
		this.backUrl = backUrl;
	}
	public Long getIdPagador() {
		return idPagador;
	}
	public void setIdPagador(Long idPagador) {
		this.idPagador = idPagador;
	}
	public String getEstadoPago() {
		return estadoPago;
	}
	public void setEstadoPago(String estadoPago) {
		this.estadoPago = estadoPago;
	}
	public Long getFechaEstado() {
		return fechaEstado;
	}
	public void setFechaEstado(Long fechaEstado) {
		this.fechaEstado = fechaEstado;
	}
	public String getNotificationUrl() {
		return notificationUrl;
	}
	public void setNotificationUrl(String notificationUrl) {
		this.notificationUrl = notificationUrl;
	}
	public Boolean getNotificado() {
		return notificado;
	}
	public void setNotificado(Boolean notificado) {
		this.notificado = notificado;
	}
	public String getIdPreferencia() {
		return idPreferencia;
	}
	public void setIdPreferencia(String idPreferencia) {
		this.idPreferencia = idPreferencia;
	}
	
}
