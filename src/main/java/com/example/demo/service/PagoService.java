package com.example.demo.service;

import java.util.ArrayList;
import java.util.Optional;

import com.example.demo.entities.Pago;

public interface PagoService {

	public Iterable<Pago> findAll();
	public ArrayList<Pago> findAllByIdAplicacion(String empresa);
	public Optional<Pago> findById(Long id);
	public Optional<Pago> findByIdAplicacion(String empresa);
	public ArrayList<Pago> findByNotificado(Boolean notificado);
	public Pago save(Pago pago);
	public void deleteById(Long id);
}
