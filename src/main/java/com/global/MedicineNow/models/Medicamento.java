package com.global.MedicineNow.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Setter
@AllArgsConstructor
@Getter
public class Medicamento {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

	@NotEmpty
	@Size(min = 3, max = 100)
    private String nome;
	
	@NotEmpty
    private String descricao;
	
	@NotEmpty
	private int quantidade;
	
	@NotEmpty
	@Size(min = 3, max = 100)
    private String fabricante;
	
	@NotEmpty
	@Size(min = 3, max = 100)
    private String principioAtivo;
	
	@NotEmpty
	@Size(min = 3, max = 100)
    private String dosagem;
	
	@OneToOne
    @JoinColumn(name = "cofre_id")
    private Cofre cofre;
	
}
