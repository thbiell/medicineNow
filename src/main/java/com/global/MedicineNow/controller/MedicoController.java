package com.global.MedicineNow.controller;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
import com.global.MedicineNow.dto.DadosListagemReceitaMedico;
import com.global.MedicineNow.dto.DadosListagemReceitaUsuario;
import com.global.MedicineNow.exceptions.RestDuplicatedException;
import com.global.MedicineNow.models.Credencial;
import com.global.MedicineNow.models.Medico;
import com.global.MedicineNow.models.Receita;
import com.global.MedicineNow.models.Usuario;
import com.global.MedicineNow.repository.MedicoRepository;
import com.global.MedicineNow.repository.ReceitaRepository;
import com.global.MedicineNow.service.TokenService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/medico")
public class MedicoController {

	@Autowired
	MedicoRepository repository;

	@Autowired
	AuthenticationManager manager;
	
	@Autowired
	ReceitaRepository receitaRepository;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	TokenService tokenService;
	
	

	@PostMapping("/cadastrar")
	public ResponseEntity<Medico> registrar(@RequestBody @Valid Medico usuario) {
		try {

			usuario.setSenha(encoder.encode(usuario.getSenha()));
			repository.save(usuario);

			return ResponseEntity.status(HttpStatus.CREATED).body(usuario);

		} catch (DataIntegrityViolationException e) {
			throw new RestDuplicatedException("Já existe um usuario com este email");
		}

	}

	@PostMapping("/login")
	public ResponseEntity<Object> login(@RequestBody @Valid Credencial credencial) {
	    Optional<Medico> medicoOptional = repository.findByEmail(credencial.email());

	    if (medicoOptional.isPresent() && encoder.matches(credencial.senha(), medicoOptional.get().getSenha())) {
	        var token = tokenService.generateToken(credencial);
	        return ResponseEntity.ok(token);
	    } else {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	    }
	}

	
	
	

	@PutMapping
	public ResponseEntity<Medico> atualizar(@RequestBody DadosAtualizacaoMedico dadosAtualizacaoUsuario) {
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			String email = authentication.getName();
			Medico usuarioSelecionado = repository.findByEmail(email).orElseThrow(
					() -> new RuntimeException("Somente o próprio usuário pode atualizar a própria conta."));
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

	@PostMapping("/cadastrar-receita")
	public ResponseEntity<Receita> cadastrarReceita(@RequestBody @Valid Receita receita) {
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			String email = authentication.getName();

			Medico medico = repository.findByEmail(email)
					.orElseThrow(() -> new RuntimeException("Médico não encontrado"));

			receita.setMedico(medico);
			
			receitaRepository.save(receita);

			return ResponseEntity.status(HttpStatus.CREATED).body(receita);
		} catch (DataIntegrityViolationException e) {
			throw new RestDuplicatedException("Erro ao cadastrar a receita");
		}
	}

	@GetMapping("/receitas")
	public ResponseEntity<List<DadosListagemReceitaMedico>> getReceitasDoMedico() {
	    try {
	        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	        String email = authentication.getName();

	        Medico medico = repository.findByEmail(email)
	                .orElseThrow(() -> new RuntimeException("Medico não encontrado."));

	        Collection<Receita> receitas = medico.getReceitas();


	        List<DadosListagemReceitaMedico> receitasDTO = receitas.stream()
	                .map(receita -> new DadosListagemReceitaMedico(
	                        receita.getId(),
	                        receita.getUsuario().getNome(),
	                        receita.getDataPrescricao(),
	                        receita.getDescricao()))
	                .collect(Collectors.toList());

	        return ResponseEntity.ok(receitasDTO);
	    } catch (DataIntegrityViolationException e) {
	        throw new RestDuplicatedException("Erro ao obter receitas do usuário.");
	    }
	}
}
