package com.example.demo.service;

import java.util.ArrayList;
import java.util.Optional;

import com.example.demo.entities.Transaccion;

public interface TransaccionService {
	public Iterable<Transaccion> findAll();
	public Optional<Transaccion> findById(Long id);
	public Transaccion save(Transaccion pago);
	public void deleteById(Long id);
	public Optional<Transaccion> findByIdPago(Long idPago);
	public ArrayList<Transaccion> findAllByIdPago(Long idPago);
}
