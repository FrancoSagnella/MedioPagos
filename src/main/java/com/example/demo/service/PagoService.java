package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.demo.entities.Pago;

public interface PagoService {

	public Iterable<Pago> findAll();
	public Iterable<Pago> findAllByIdConsumidor(Long empresa);
	public Optional<Pago> findById(Long id);
	public Optional<Pago> findByIdConsumidor(Long empresa);
	public ArrayList<Pago> findByNotificado(Boolean notificado);
	public Pago save(Pago pago);
	public void deleteById(Long id);
}
