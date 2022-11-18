package com.example.demo.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.Pagador;

@Repository
public interface PagadorRepository extends JpaRepository<Pagador, UUID> {
	public Optional<Pagador> findByDniAndTipodni(Long dni, String tipodni);
}
