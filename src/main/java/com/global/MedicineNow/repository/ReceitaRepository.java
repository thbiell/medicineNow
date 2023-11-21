package com.global.MedicineNow.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.global.MedicineNow.models.Receita;

@Repository
public interface ReceitaRepository extends JpaRepository<Receita, Long> {
    
	List<Receita> findByMedicoId(Long medicoId);
	List<Receita> findByUsuarioId(Long usuarioId);
}
