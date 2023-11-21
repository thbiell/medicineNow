package com.global.MedicineNow.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.global.MedicineNow.models.Medico;

@Repository
public interface MedicoRepository extends JpaRepository<Medico, Long> {
   
	Optional<Medico> findByEmail(String email);
	
}