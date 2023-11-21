package com.global.MedicineNow.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.global.MedicineNow.models.Credencial;
import com.global.MedicineNow.models.Token;
import com.global.MedicineNow.models.Medico;
import com.global.MedicineNow.repository.MedicoRepository;




@Service
public class TokenServiceMedico {

    @Autowired
    MedicoRepository usuarioRepository;

    @Value("jwt.secret")
    String secret;

    public Token generateToken( Credencial credencial) {
        Algorithm alg = Algorithm.HMAC256(secret);
        var token = JWT.create()
                    .withSubject(credencial.email())
                    .withExpiresAt(Instant.now().plus(1, ChronoUnit.HOURS))
                    .withIssuer("MedicineNow!")
                    .sign(alg);

        return new Token(token, "JWT", "Bearer");
    }

    public Medico valideAndGetUserBy(String token) {
        Algorithm alg = Algorithm.HMAC256(secret);
        var email =  JWT.require(alg)
            .withIssuer("MedicineNow!")
            .build()
            .verify(token)
            .getSubject()
            ;

        return usuarioRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

}