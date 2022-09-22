package com.example.demo.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "pagadores")
public class Pagador {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long dni;
	private String nombreApellidoRazonSocial;
	private String correo;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNombreApellidoRazonSocial() {
		return nombreApellidoRazonSocial;
	}
	public void setNombreApellidoRazonSocial(String nombreApellidoRazonSocial) {
		this.nombreApellidoRazonSocial = nombreApellidoRazonSocial;
	}
	public String getCorreo() {
		return correo;
	}
	public void setCorreo(String correo) {
		this.correo = correo;
	}
	public Long getDni() {
		return dni;
	}
	public void setDni(Long dni) {
		this.dni = dni;
	}
}
