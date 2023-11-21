package com.global.MedicineNow.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.global.MedicineNow.exceptions.RestDuplicatedException;
import com.global.MedicineNow.models.Cofre;
import com.global.MedicineNow.models.Farmacia;
import com.global.MedicineNow.models.Medicamento;
import com.global.MedicineNow.repository.CofreRepository;
import com.global.MedicineNow.repository.FarmaciaRepository;
import com.global.MedicineNow.repository.MedicamentoRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/manager")
public class ManagerController {

    @Autowired
    private MedicamentoRepository remedioRepository;

    @Autowired
    private FarmaciaRepository farmaciaRepository;

    @Autowired
    private CofreRepository cofreRepository;

 

    @PostMapping("/medicamentos")
    public ResponseEntity<Medicamento> cadastrarRemedio(@RequestBody @Valid Medicamento remedio) {
        try {
            remedioRepository.save(remedio);
            return ResponseEntity.status(HttpStatus.CREATED).body(remedio);
        } catch (DataIntegrityViolationException e) {
            throw new RestDuplicatedException("Já existe um remédio com este nome.");
        }
    }

    @GetMapping("/medicamentos")
    public ResponseEntity<List<Medicamento>> getRemedios() {
        try {
            List<Medicamento> remedios = remedioRepository.findAll();
            return ResponseEntity.ok(remedios);
        } catch (DataIntegrityViolationException e) {
            throw new RestDuplicatedException("Erro ao obter remédios.");
        }
    }

  

    @PostMapping("/farmacias")
    public ResponseEntity<Farmacia> cadastrarFarmacia(@RequestBody @Valid Farmacia farmacia) {
        try {
            farmaciaRepository.save(farmacia);
            return ResponseEntity.status(HttpStatus.CREATED).body(farmacia);
        } catch (DataIntegrityViolationException e) {
            throw new RestDuplicatedException("Já existe uma farmácia com este nome.");
        }
    }

    @GetMapping("/farmacias")
    public ResponseEntity<List<Farmacia>> getFarmacias() {
        try {
            List<Farmacia> farmacias = farmaciaRepository.findAll();
            return ResponseEntity.ok(farmacias);
        } catch (DataIntegrityViolationException e) {
            throw new RestDuplicatedException("Erro ao obter farmácias.");
        }
    }


    @PostMapping("/cofres")
    public ResponseEntity<Cofre> cadastrarCofre(@RequestBody @Valid Cofre cofre) {
        try {
            cofreRepository.save(cofre);
            return ResponseEntity.status(HttpStatus.CREATED).body(cofre);
        } catch (DataIntegrityViolationException e) {
            throw new RestDuplicatedException("Já existe um cofre com este nome.");
        }
    }

    @GetMapping("/cofres")
    public ResponseEntity<List<Cofre>> getCofres() {
        try {
            List<Cofre> cofres = cofreRepository.findAll();
            return ResponseEntity.ok(cofres);
        } catch (DataIntegrityViolationException e) {
            throw new RestDuplicatedException("Erro ao obter cofres.");
        }
    }


}