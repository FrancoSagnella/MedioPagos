package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.Aplicacion;

@Repository
public interface AplicacionRepository extends JpaRepository<Aplicacion, Long>{
	Optional<Aplicacion> findByToken(String token);
}
