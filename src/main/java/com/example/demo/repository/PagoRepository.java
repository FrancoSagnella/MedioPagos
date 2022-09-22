package com.example.demo.repository;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.demo.entities.Pago;
@Repository
public interface PagoRepository extends JpaRepository<Pago, Long>{
	Optional<Pago> findByIdAplicacion(String idConsumidor);
	ArrayList<Pago> findAllByIdAplicacion(String idConsumidor);
	ArrayList<Pago> findByNotificado(Boolean notificado);
}
