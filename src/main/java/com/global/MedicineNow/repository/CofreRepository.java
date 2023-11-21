package com.global.MedicineNow.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.global.MedicineNow.models.Cofre;

@Repository
public interface CofreRepository extends JpaRepository<Cofre, Long> {

    List<Cofre> findAll();

    Optional<Cofre> findByNome(String nome);
}
