package com.example.demo.service;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entities.Transaccion;
import com.example.demo.repository.TransaccionRepository;

@Service
public class TransaccionServiceImp implements TransaccionService{
	
	@Autowired
	private TransaccionRepository Transaccion;
	
	@Override
	@Transactional(readOnly = true)
	public Iterable<Transaccion> findAll() {
		return Transaccion.findAll();
	}
	
	@Override
	@Transactional(readOnly = true)
	public ArrayList<Transaccion> findAllByIdPago(Long idPago) {
		return Transaccion.findAllByIdPago(idPago);
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<Transaccion> findById(Long id) {
		return Transaccion.findById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<Transaccion> findByIdPago(Long idPago) {
		return Transaccion.findByIdPago(idPago);
	}
	
	@Override
	@Transactional
	public Transaccion save(Transaccion pago) {
		return Transaccion.save(pago);
	}

	@Override
	@Transactional
	public void deleteById(Long id) {
		Transaccion.deleteById(id);
	}
}
