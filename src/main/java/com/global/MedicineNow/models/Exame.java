package com.global.MedicineNow.models;

import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Exame {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Medico medico;

    @Override
	public int hashCode() {
		return Objects.hash(id, medico, paciente, resultado, tipo);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Exame other = (Exame) obj;
		return Objects.equals(id, other.id) && Objects.equals(medico, other.medico)
				&& Objects.equals(paciente, other.paciente) && Objects.equals(resultado, other.resultado)
				&& Objects.equals(tipo, other.tipo);
	}
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
	public Usuario getPaciente() {
		return paciente;
	}
	public void setPaciente(Usuario paciente) {
		this.paciente = paciente;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public String getResultado() {
		return resultado;
	}
	public void setResultado(String resultado) {
		this.resultado = resultado;
	}
	@ManyToOne
    private Usuario paciente;

    private String tipo;
    private String resultado;

	public Exame(Long id, Medico medico, Usuario paciente, String tipo, String resultado) {
		super();
		this.id = id;
		this.medico = medico;
		this.paciente = paciente;
		this.tipo = tipo;
		this.resultado = resultado;
	}

	public Exame() {
		
	}
	@Override
	public String toString() {
		return "Exame [id=" + id + ", medico=" + medico + ", paciente=" + paciente + ", tipo=" + tipo + ", resultado="
				+ resultado + "]";
	}
    
	
}
