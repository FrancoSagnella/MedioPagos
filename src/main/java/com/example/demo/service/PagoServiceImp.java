package com.example.demo.service;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entities.Pago;
import com.example.demo.repository.PagoRepository;

@Service
public class PagoServiceImp implements PagoService{

	@Autowired
	private PagoRepository pagoRepository;
	
	@Override
	@Transactional(readOnly = true)
	public Iterable<Pago> findAll() {
		return pagoRepository.findAll();
	}
	
	@Override
	@Transactional(readOnly = true)
	public ArrayList<Pago> findAllByIdAplicacion(String empresa) {
		return pagoRepository.findAllByIdAplicacion(empresa);
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<Pago> findById(Long id) {
		return pagoRepository.findById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<Pago> findByIdAplicacion(String id) {
		return pagoRepository.findByIdAplicacion(id);
	}
	
	@Override
	@Transactional(readOnly = true)
	public ArrayList<Pago> findByNotificado(Boolean notificado) {
		
		return pagoRepository.findByNotificado(notificado);
	}
	
	@Override
	@Transactional
	public Pago save(Pago pago) {
		return pagoRepository.save(pago);
	}

	@Override
	@Transactional
	public void deleteById(Long id) {
		pagoRepository.deleteById(id);
	}
	
	@Override
	@Transactional
	public Optional<Pago> findByIdTransaccionAplicacion(String idTransaccionAplicacion){
		return pagoRepository.findByIdTransaccionAplicacion(idTransaccionAplicacion);
	}
}
