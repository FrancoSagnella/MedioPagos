package com.example.demo.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entities.Consumidor;
import com.example.demo.repository.ConsumidorRepository;


@Service
public class ConsumidorServiceImp implements ConsumidorService{
	@Autowired
	private ConsumidorRepository pagoRepository;
	
	@Override
	@Transactional(readOnly = true)
	public Iterable<Consumidor> findAll() {
		return pagoRepository.findAll();
	}

	
	@Override
	@Transactional(readOnly = true)
	public Optional<Consumidor> findById(Long id) {
		return pagoRepository.findById(id);
	}
	
	
	@Override
	@Transactional
	public Consumidor save(Consumidor pago) {
		return pagoRepository.save(pago);
	}

	@Override
	@Transactional
	public void deleteById(Long id) {
		pagoRepository.deleteById(id);
	}
	
	@Override
	@Transactional
	public Optional<Consumidor> findByToken(String token)
	{
		return pagoRepository.findByToken(token);
	}
}
