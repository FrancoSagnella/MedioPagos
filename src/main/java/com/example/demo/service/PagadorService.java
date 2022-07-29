package com.example.demo.service;

import java.util.Optional;

import com.example.demo.entities.Pagador;

public interface PagadorService {
	public Iterable<Pagador> findAll();
	public Optional<Pagador> findById(Long id);
	public Pagador save(Pagador producto);
	public void deleteById(Long id);
}
