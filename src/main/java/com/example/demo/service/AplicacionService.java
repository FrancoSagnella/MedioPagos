package com.example.demo.service;

import java.util.Optional;

import com.example.demo.entities.Aplicacion;

public interface AplicacionService {
	public Iterable<Aplicacion> findAll();
	public Optional<Aplicacion> findById(Long id);
	public Aplicacion save(Aplicacion pago);
	public void deleteById(Long id);
	public Optional<Aplicacion> findByToken(String token);
}
