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

    @PostMapping("/cadastrar")
    public ResponseEntity<Medico> registrar(@RequestBody @Valid Medico medico) {
        try {
            
            medico.setSenha(encoder.encode(medico.getSenha()));
            repository.save(medico);
            return ResponseEntity.status(HttpStatus.CREATED).body(medico);
        } catch (DataIntegrityViolationException e) {
            throw new RestDuplicatedException("Já existe um médico com este CRM ou email");
        }
    }

    @GetMapping("/login")
    public String login(@RequestBody @Valid Credencial credencial) {
        Optional<Medico> medicoOptional = repository.findByEmail(credencial.email());

        if (medicoOptional.isPresent()) {
            Medico medico = medicoOptional.get();
            if (encoder.matches(credencial.senha(), medico.getSenha())) {
            	System.out.println("funcionou");
                return "redirect:/home"; // Redirecionar para a página home
            }
        }

        return "redirect:/login?error=true"; // Redirecionar para a página de login com um parâmetro de erro
    }

	
	@PutMapping
	public ResponseEntity<Medico> atualizar(@RequestBody DadosAtualizacaoMedico dadosAtualizacaoUsuario) {
	    try {
	        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	        String email = authentication.getName();

	        Medico usuarioSelecionado = repository.findByEmail(email)
	                .orElseThrow(() -> new RuntimeException("Somente o próprio usuário pode atualizar a própria conta."));

	        if (dadosAtualizacaoUsuario.senha() != null) {
	            usuarioSelecionado.setSenha(encoder.encode(dadosAtualizacaoUsuario.senha()));
	        }
	        if (dadosAtualizacaoUsuario.nome() != null) {
	            usuarioSelecionado.setNome(dadosAtualizacaoUsuario.nome());
	        }
	        if (dadosAtualizacaoUsuario.sobrenome() != null) {
	            usuarioSelecionado.setSobrenome(dadosAtualizacaoUsuario.sobrenome());
	        }
	        if (dadosAtualizacaoUsuario.email() != null) {
	            usuarioSelecionado.setEmail(dadosAtualizacaoUsuario.email());
	        }
	        if (dadosAtualizacaoUsuario.telefone() != null) {
	            usuarioSelecionado.setTelefone(dadosAtualizacaoUsuario.telefone());
	        }
	        if (dadosAtualizacaoUsuario.hospital() != null) {
	            usuarioSelecionado.setHospital(dadosAtualizacaoUsuario.hospital());
	        }
	        if (dadosAtualizacaoUsuario.idade() != null) {
	            usuarioSelecionado.setIdade(dadosAtualizacaoUsuario.idade());
	        }

	        repository.save(usuarioSelecionado);

	        return ResponseEntity.status(HttpStatus.OK).body(usuarioSelecionado);

	    } catch (DataIntegrityViolationException e) {
	        throw new RestDuplicatedException("Já existe um usuário com este email ou telefone");
	    }
	

	}

}