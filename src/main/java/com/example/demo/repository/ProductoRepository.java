package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.entities.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Long>{
	Iterable<Producto> findAllByPago(Long pago_id);
}
