package com.global.MedicineNow.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.global.MedicineNow.models.Usuario;


@Repository
public interface UserRepository extends JpaRepository<Usuario, String>{

	Optional<Usuario> findByEmail(String username);

	
}