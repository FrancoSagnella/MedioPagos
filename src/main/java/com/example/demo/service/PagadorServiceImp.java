package com.example.demo.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entities.Pagador;
import com.example.demo.repository.PagadorRepository;

@Service
public class PagadorServiceImp implements PagadorService{
	
	@Autowired
	private PagadorRepository pagadorRepository;
	
	@Override
	public Iterable<Pagador> findAll() {
		return pagadorRepository.findAll();
	}

	@Override
	public Optional<Pagador> findById(Long id) {
		return pagadorRepository.findById(id);
	}

	@Override
	public Pagador save(Pagador producto) {
		return pagadorRepository.save(producto);
	}

	@Override
	public void deleteById(Long id) {
		pagadorRepository.deleteById(id);
	}
	
}
