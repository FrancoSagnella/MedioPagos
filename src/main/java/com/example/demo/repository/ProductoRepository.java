package com.example.demo.repository;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.entities.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Long>{
	ArrayList<Producto> findAllByPago(Long pago_id);
}
