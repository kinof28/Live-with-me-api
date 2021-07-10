package com.Abdelwahab.Live.With.ME.services;

import com.Abdelwahab.Live.With.ME.entities.Admin;
import com.Abdelwahab.Live.With.ME.repositories.AdminRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class AdminDetailsServiceImp implements UserDetailsService {
    private AdminRepository adminRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Admin> adminO=this.adminRepository.findByEmail(email);
        if(adminO.isPresent()){
            return adminO.get();
        }
        else throw new UsernameNotFoundException("there is no admin with email "+email);
    }
}
