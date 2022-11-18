package com.example.demo.service;

import java.util.Optional;
import java.util.UUID;

import com.example.demo.entities.Pagador;

public interface PagadorService {
	public Iterable<Pagador> findAll();
	public Optional<Pagador> findById(UUID id);
	public Optional<Pagador> findByDniAndTipodni(Long dni, String tipodni);
	public Pagador save(Pagador producto);
	public void deleteById(UUID id);
}
