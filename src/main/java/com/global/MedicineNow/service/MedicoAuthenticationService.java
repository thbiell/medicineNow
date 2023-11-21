package com.global.MedicineNow.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.global.MedicineNow.repository.MedicoRepository;

@Service
public class MedicoAuthenticationService implements UserDetailsService {

    @Autowired
    private MedicoRepository medicoRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return medicoRepository.findByEmail(username)
            .orElseThrow(() -> new UsernameNotFoundException("Médico não encontrado"));
    }
}
