package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.demo.entities.ConciliacionPrisma;

@Repository
public interface ConciliacionPrismaRepository extends JpaRepository<ConciliacionPrisma, Integer>{

}
