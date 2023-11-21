package com.global.MedicineNow.models;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Data
@NoArgsConstructor
@Setter
@AllArgsConstructor
@Getter
public class Usuario implements UserDetails {


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	 private Long id;
     
	@NotEmpty
    @Size(min = 3, max = 100)
     private String nome;
	
	@NotEmpty
	@Size(min = 3, max = 100)
    private String sobrenome;
	
    @NotEmpty
    @Email
    @Column(unique=true)
     private String email;
     
    @NotEmpty
     private String senha;
     
    @NotEmpty 
    @Size(min = 7, max = 15)
     private String telefone;
    
    @NotEmpty
    @Size(min = 16, max = 16)
    private String cartaoSus;
    
    @NotEmpty
    @Size(min = 8, max = 9)
    private String cep;
    
    @NotEmpty
    @Size(min = 0, max = 3)
    private String idade;
    
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<Receita> receitas;

	public List<Receita> getReceitas() {
		return receitas;
	}

	public void setReceitas(List<Receita> receitas) {
		this.receitas = receitas;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSenha() {
		return senha;
	}

	public String getCartaoSus() {
		return cartaoSus;
	}

	public void setCartaoSus(String cartaoSus) {
		this.cartaoSus = cartaoSus;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	

	public String getIdade() {
		return idade;
	}

	public void setIdade(String idade) {
		this.idade = idade;
	}

	public String getSobrenome() {
		return sobrenome;
	}

	public void setSobrenome(String sobrenome) {
		this.sobrenome = sobrenome;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		 return List.of();
	}

    @Override
    public String getPassword() {
        return this.senha;
    }

    @Override
    public String getUsername() {
        return this.email;
    }
	
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
    
    

}

