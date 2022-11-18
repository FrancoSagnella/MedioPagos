package com.example.demo.entities;

import java.util.Map;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "pagadores")
public class Pagador {
	
	@Id
	private UUID id;
	private Long dni;
	private String nombreApellidoRazonSocial;
	private String correo;
	private String tipodni;
	private String direccion;
	
	public UUID getId() {
		return id;
	}
	public void setId(UUID id) {
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
	public String getTipoDni() {
		return tipodni;
	}
	public void setTipoDni(String tipoDni) {
		this.tipodni = tipoDni;
	}
	
	public static boolean pagadorExists(Long dni, String tipoDni, Iterable<Pagador> pagadores) {
		
		boolean ret = false;

		for(Pagador item:pagadores)
		{
			if(item.dni.equals(dni) && item.tipodni.equals(tipoDni))
			{
				ret = true;
				break;
			}
		}

		return ret;
	}
	public String getDireccion() {
		return direccion;
	}
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
}
