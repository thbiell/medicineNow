package com.global.MedicineNow.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record DadosAtualizacaoUsuario(

			@NotEmpty
			@Size(min = 3, max = 100)
			String nome,

			@NotEmpty
			@Size(min = 3, max = 100)
			 String sobrenome,

			@NotEmpty
			@Email
			@Column(unique = true)
			 String email,

			@NotEmpty
			@Size(min = 6, max = 255)
			String senha,

			@NotEmpty
			@Pattern(regexp = "\\d{10,11}")
			String telefone,

			@NotEmpty
			@Size(min = 16, max = 16)
			String cartaoSus,

			@NotEmpty
			@Size(min = 8, max = 9)
			String cep,
			
			@Pattern(regexp = "^\\d{2}$")
	        String idade
		
		
		) {

}
