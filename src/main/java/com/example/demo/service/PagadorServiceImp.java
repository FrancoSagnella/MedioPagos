package com.example.demo.service;

import java.util.Optional;
import java.util.UUID;

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
	public Optional<Pagador> findById(UUID id) {
		return pagadorRepository.findById(id);
	}

	@Override
	public Pagador save(Pagador producto) {
		return pagadorRepository.save(producto);
	}

	@Override
	public void deleteById(UUID id) {
		pagadorRepository.deleteById(id);
	}
	
	public Optional<Pagador> findByDniAndTipodni(Long dni, String tipodni){
		return pagadorRepository.findByDniAndTipodni(dni, tipodni);
	}
	
}
