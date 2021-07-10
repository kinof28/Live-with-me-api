package com.Abdelwahab.Live.With.ME.services;

import com.Abdelwahab.Live.With.ME.entities.Client;
import com.Abdelwahab.Live.With.ME.repositories.ClientRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserDetailsServiceImp implements UserDetailsService {
    private ClientRepository clientR;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Client> clientO=this.clientR.findByEmail(email);
        if(clientO.isPresent())
            return clientO.get();
        else throw new UsernameNotFoundException("there is no user with email "+email);
    }
}
