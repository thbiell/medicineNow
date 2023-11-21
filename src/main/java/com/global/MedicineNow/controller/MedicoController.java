package com.global.MedicineNow.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.global.MedicineNow.dto.DadosAtualizacaoMedico;
import com.global.MedicineNow.exceptions.RestDuplicatedException;
import com.global.MedicineNow.models.Credencial;
import com.global.MedicineNow.models.Medico;
import com.global.MedicineNow.repository.MedicoRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/medico")
public class MedicoController {

    @Autowired
    MedicoRepository repository;

    @Autowired
    PasswordEncoder encoder;

    @GetMapping("/registrar")
    public String exibirFormularioRegistro() {
        // Lógica para exibir o formulário de registro
        return "registro/formularioRegistro"; // Nome do arquivo HTML (sem extensão)
    }

    @PostMapping("/registrar")
    public String processarRegistro(@RequestBody @Valid Medico medico) {
        try {
            medico.setSenha(encoder.encode(medico.getSenha()));
            repository.save(medico);
            return "redirect:/login"; // Redirecionar para a página de login após o registro
        } catch (DataIntegrityViolationException e) {
            throw new RestDuplicatedException("Já existe um médico com este CRM ou email");
        }
    }
    
    
    @GetMapping("/login")
    public String exibirLogin() {
        // Lógica para exibir o formulário de registro
        return "login/formularioLogin"; // Nome do arquivo HTML (sem extensão)
    }
    
    
    @PostMapping("/login")
    public String processarLogin(@RequestBody @Valid Credencial credencial) {
        Optional<Medico> medicoOptional = repository.findByEmail(credencial.email());

        if (medicoOptional.isPresent()) {
            Medico medico = medicoOptional.get();
            if (encoder.matches(credencial.senha(), medico.getSenha())) {
                System.out.println("funcionou");
                return "redirect:/home";
            }
        }

        return "redirect:/login?error=true";
    }

    @GetMapping("/atualizar")
    public String exibirFormularioAtualizacao() {
        // Lógica para exibir o formulário de atualização
        return "atualizacao/formularioAtualizacao"; // Nome do arquivo HTML (sem extensão)
    }

    @PostMapping("/atualizar")
    public String processarAtualizacao(@RequestBody DadosAtualizacaoMedico dadosAtualizacaoUsuario) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();

            Medico medico = repository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Somente o próprio médico pode atualizar a própria conta."));

            if (dadosAtualizacaoUsuario.senha() != null) {
                medico.setSenha(encoder.encode(dadosAtualizacaoUsuario.senha()));
            }
            if (dadosAtualizacaoUsuario.nome() != null) {
                medico.setNome(dadosAtualizacaoUsuario.nome());
            }
            if (dadosAtualizacaoUsuario.sobrenome() != null) {
                medico.setSobrenome(dadosAtualizacaoUsuario.sobrenome());
            }
            if (dadosAtualizacaoUsuario.email() != null) {
                medico.setEmail(dadosAtualizacaoUsuario.email());
            }
            if (dadosAtualizacaoUsuario.telefone() != null) {
                medico.setTelefone(dadosAtualizacaoUsuario.telefone());
            }
            if (dadosAtualizacaoUsuario.hospital() != null) {
                medico.setHospital(dadosAtualizacaoUsuario.hospital());
            }
            if (dadosAtualizacaoUsuario.idade() != null) {
                medico.setIdade(dadosAtualizacaoUsuario.idade());
            }

            repository.save(medico);

            return "redirect:/home"; // Redirecionar para a página inicial após a atualização
        } catch (DataIntegrityViolationException e) {
            throw new RestDuplicatedException("Já existe um médico com este email ou telefone");
        }
    }
}