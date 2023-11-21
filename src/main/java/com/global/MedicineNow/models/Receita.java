package com.global.MedicineNow.models;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
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
public class Receita {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "medico_id") 
    private Medico medico;


    @NotNull
    private Date dataPrescricao;

    @NotNull
    private String descricao;


    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;
    
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Medico getMedico() {
		return medico;
	}
	public void setMedico(Medico medico) {
		this.medico = medico;
	
	}
	public Date getDataPrescricao() {
		return dataPrescricao;
	}
	public void setDataPrescricao(Date dataPrescricao) {
		this.dataPrescricao = dataPrescricao;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
    
}
