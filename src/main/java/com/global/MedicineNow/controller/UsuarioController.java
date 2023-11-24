package com.global.MedicineNow.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.global.MedicineNow.dto.DadosAtualizacaoUsuario;
import com.global.MedicineNow.dto.DadosListagemCofres;
import com.global.MedicineNow.dto.DadosListagemFarmacia;
import com.global.MedicineNow.dto.DadosListagemReceitaUsuario;
import com.global.MedicineNow.dto.RetiradaMedicamento;
import com.global.MedicineNow.exceptions.RestDuplicatedException;
import com.global.MedicineNow.exceptions.TratadorDeErros;
import com.global.MedicineNow.models.Cofre;
import com.global.MedicineNow.models.Credencial;
import com.global.MedicineNow.models.Farmacia;
import com.global.MedicineNow.models.Receita;
import com.global.MedicineNow.models.Usuario;
import com.global.MedicineNow.repository.UserRepository;
import com.global.MedicineNow.repository.CofreRepository;
import com.global.MedicineNow.repository.FarmaciaRepository;
import com.global.MedicineNow.repository.ReceitaRepository;
import com.global.MedicineNow.service.TokenService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/usuario")
public class UsuarioController {

	@Autowired
	UserRepository repository;
	
	@Autowired
	ReceitaRepository receitaRepository;
	
	@Autowired
	CofreRepository cofreRepository;

	@Autowired
	AuthenticationManager manager;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
    FarmaciaRepository farmaciaRepository;

	
	@Autowired
	TokenService tokenService;

	@PostMapping("/cadastrar")
	public ResponseEntity<Usuario> registrar(@RequestBody @Valid Usuario usuario) {
		try {

			usuario.setSenha(encoder.encode(usuario.getSenha()));
			repository.save(usuario);

			return ResponseEntity.status(HttpStatus.CREATED).body(usuario);

		} catch (DataIntegrityViolationException e) {
			throw new RestDuplicatedException("Já existe um usuario com este email ou telefone");
		}

	}

	@PostMapping("/login")
	public ResponseEntity<Object> login(@RequestBody @Valid Credencial credencial) {
		manager.authenticate(credencial.toAuthentication());
		var token = tokenService.generateToken(credencial);
		return ResponseEntity.ok(token);
	}

	
	@PutMapping
	public ResponseEntity<Usuario> atualizar(@RequestBody DadosAtualizacaoUsuario dadosAtualizacaoUsuario) {
	    try {
	        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	        String email = authentication.getName();

	        Usuario usuarioSelecionado = repository.findByEmail(email)
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
	        if (dadosAtualizacaoUsuario.cartaoSus() != null) {
	            usuarioSelecionado.setCartaoSus(dadosAtualizacaoUsuario.cartaoSus());
	        }
	        if (dadosAtualizacaoUsuario.cep() != null) {
	            usuarioSelecionado.setCep(dadosAtualizacaoUsuario.cep());
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
	
	
	@GetMapping("/receitas")
	public ResponseEntity<List<DadosListagemReceitaUsuario>> getReceitasDoUsuario() {
	    try {
	        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	        String email = authentication.getName();

	        Usuario usuario = repository.findByEmail(email)
	                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

	        List<Receita> receitas = receitaRepository.findByUsuarioId(usuario.getId());

	        List<DadosListagemReceitaUsuario> receitasDTO = receitas.stream()
	                .map(receita -> new DadosListagemReceitaUsuario(
	                        receita.getId(),
	                        receita.getMedico().getNome(), 
	                        receita.getDataPrescricao(),
	                        receita.getDescricao()))
	                .collect(Collectors.toList());

	        return ResponseEntity.ok(receitasDTO);
	    } catch (DataIntegrityViolationException e) {
	        throw new RestDuplicatedException("Erro ao obter receitas do usuário.");
	    }
	}

	
	@GetMapping("/cofres")
	public ResponseEntity<List<DadosListagemCofres>> getCofres() {
	    try {
	        List<Cofre> cofres = cofreRepository.findAll();

	        List<DadosListagemCofres> response = cofres.stream()
	                .map(cofre -> {
	                    List<DadosListagemCofres.MedicamentoDTO> medicamentosDTO = cofre.getMedicamento() != null
	                            ? List.of(new DadosListagemCofres.MedicamentoDTO(
	                                    cofre.getMedicamento().getId(),
	                                    cofre.getMedicamento().getNome(),
	                                    cofre.getMedicamento().getQuantidade()))
	                            : List.of();

	                    return new DadosListagemCofres(
	                            cofre.getId(),
	                            cofre.getNome(),
	                            medicamentosDTO);
	                })
	                .collect(Collectors.toList());

	        return ResponseEntity.ok(response);
	    } catch (DataIntegrityViolationException e) {
	        throw new RestDuplicatedException("Erro ao obter cofres.");
	    }
	}
	
	@PostMapping("/marcarRetirada")
	public ResponseEntity<String> marcarRetirada(@RequestBody RetiradaMedicamento retiradaMedicamento) {
	    try {
	        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	        String email = authentication.getName();

	        Usuario usuario = repository.findByEmail(email)
	                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

	        boolean medicamentoValido = validarMedicamento(retiradaMedicamento.nomeRemedio(), retiradaMedicamento.nomeCofre());
	        if (!medicamentoValido) {
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("O medicamento não está disponível neste cofre.");
	        }

	        boolean receitaValida = validarCodigoReceita(retiradaMedicamento.codigoReceita(), usuario.getId());
	        if (!receitaValida) {
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Código de receita inválido.");
	        }

	        String codigoRetirada = gerarCodigoRetirada();
	        Date dataRetirada = calcularDataRetirada();

	        String mensagem = String.format("Retirada marcada com sucesso. Código: %s. Data de retirada: %s", codigoRetirada, formatarData(dataRetirada));
	        return ResponseEntity.status(HttpStatus.OK).body(mensagem);
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao marcar a retirada");
	    }
	}

	private boolean validarMedicamento(String nomeMedicamento, String nomeCofre) {
	    Optional<Cofre> cofre = cofreRepository.findByNome(nomeCofre);
	    
	    if (cofre.isPresent()) {
	        return cofre.get().getMedicamento() != null && cofre.get().getMedicamento().getNome().equals(nomeMedicamento);
	    }

	    return false;
	}

	private boolean validarCodigoReceita(Long codigoReceita, Long usuarioId) {
    Optional<Receita> receita = receitaRepository.findById(codigoReceita);
    
    return receita.map(r -> r.getUsuario().getId().equals(usuarioId)).orElse(false);
}


    private String gerarCodigoRetirada() {
        Random random = new Random();
        return String.format("%08d", random.nextInt(10000));
    }

    private Date calcularDataRetirada() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 3);
        return calendar.getTime();
    }

    private String formatarData(Date data) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return sdf.format(data);
    }
    
    @GetMapping("/farmacias")
    public ResponseEntity<?> getFarmacias() {
        try {
            List<Farmacia> farmacias = farmaciaRepository.findAll();

            List<DadosListagemFarmacia> response = farmacias.stream()
                    .map(farmacia -> new DadosListagemFarmacia(
                            farmacia.getId(),
                            farmacia.getNome(),
                            farmacia.getEndereco()
                    ))
                    .collect(Collectors.toList());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao obter farmácias.");
        }
    }


}
