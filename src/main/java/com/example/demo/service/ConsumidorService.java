package com.example.demo.service;

import java.util.Optional;

import com.example.demo.entities.Consumidor;

public interface ConsumidorService {
	public Iterable<Consumidor> findAll();
	public Optional<Consumidor> findById(Long id);
	public Consumidor save(Consumidor pago);
	public void deleteById(Long id);
	public Optional<Consumidor> findByToken(String token);
}
