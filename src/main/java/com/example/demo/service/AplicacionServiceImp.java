package com.example.demo.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entities.Aplicacion;
import com.example.demo.repository.AplicacionRepository;


@Service
public class AplicacionServiceImp implements AplicacionService{
	@Autowired
	private AplicacionRepository pagoRepository;
	
	@Override
	@Transactional(readOnly = true)
	public Iterable<Aplicacion> findAll() {
		return pagoRepository.findAll();
	}

	
	@Override
	@Transactional(readOnly = true)
	public Optional<Aplicacion> findById(Long id) {
		return pagoRepository.findById(id);
	}
	
	
	@Override
	@Transactional
	public Aplicacion save(Aplicacion pago) {
		return pagoRepository.save(pago);
	}

	@Override
	@Transactional
	public void deleteById(Long id) {
		pagoRepository.deleteById(id);
	}
	
	@Override
	@Transactional
	public Optional<Aplicacion> findByToken(String token)
	{
		return pagoRepository.findByToken(token);
	}
}
