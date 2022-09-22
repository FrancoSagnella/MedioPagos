package com.example.demo.service;


import java.util.ArrayList;
import java.util.Optional;
import com.example.demo.entities.Producto;

public interface ProductoService {
	public Iterable<Producto> findAll();
	public ArrayList<Producto> findAllByPago(Long pago_id);
	public Optional<Producto> findById(Long id);
	public Producto save(Producto producto);
	public void deleteById(Long id);
}
