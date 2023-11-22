package com.global.MedicineNow.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.global.MedicineNow.models.Credencial;
import com.global.MedicineNow.models.Medico;
import com.global.MedicineNow.models.Token;
import com.global.MedicineNow.models.Usuario;
import com.global.MedicineNow.repository.MedicoRepository;
import com.global.MedicineNow.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;


@Service
public class TokenService {

    @Autowired
    UserRepository usuarioRepository;
    

    @Autowired
    MedicoRepository medicoRepository;
    

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

    public Usuario validateAndGetUserBy(String token) {
        Algorithm alg = Algorithm.HMAC256(secret);
        try {
            var email = JWT.require(alg)
                    .withIssuer("MedicineNow!")
                    .build()
                    .verify(token)
                    .getSubject();

            return usuarioRepository.findByEmail(email).orElse(null);
        } catch (Exception e) {
            // Handle exceptions, e.g., TokenExpiredException
            return null;
        }
    }

    public Medico validateAndGetMedicoBy(String token) {
        Algorithm alg = Algorithm.HMAC256(secret);
        try {
            var email = JWT.require(alg)
                    .withIssuer("MedicineNow!")
                    .build()
                    .verify(token)
                    .getSubject();

            return medicoRepository.findByEmail(email).orElse(null);
        } catch (Exception e) {
            // Handle exceptions, e.g., TokenExpiredException
            return null;
        }
    }


}