package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.demo.entities.ConciliacionMP;

@Repository
public interface ConciliacionMPRepository extends JpaRepository<ConciliacionMP, Integer>{

}
