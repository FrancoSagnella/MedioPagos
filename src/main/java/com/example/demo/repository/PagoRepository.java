package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.demo.entities.Pago;
@Repository
public interface PagoRepository extends JpaRepository<Pago, Long>{
	Optional<Pago> findByIdConsumidor(Long idConsumidor);
	Iterable<Pago> findAllByIdConsumidor(Long idConsumidor);
}
