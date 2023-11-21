package com.global.MedicineNow.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
public class Cofre {

	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

	@NotEmpty
	@Column(unique=true)
    private String localizacao;

	@NotEmpty
	@Size(min = 10, max = 100)
	private String nome;
	
	@NotEmpty
    private boolean ativo; 

    @OneToOne(mappedBy = "cofre")
    private Medicamento medicamento;

}
