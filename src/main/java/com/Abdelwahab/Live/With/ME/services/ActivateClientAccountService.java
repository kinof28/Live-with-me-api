package com.Abdelwahab.Live.With.ME.services;

import com.Abdelwahab.Live.With.ME.entities.Client;
import com.Abdelwahab.Live.With.ME.entities.ClientConfirmationToken;
import com.Abdelwahab.Live.With.ME.repositories.ClientConfirmationTokenRepository;
import com.Abdelwahab.Live.With.ME.repositories.ClientRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ActivateClientAccountService {
    private ClientConfirmationTokenRepository tokenRepository;
    private ClientRepository clientRepository;
    public ClientConfirmationToken createToken(Client client){
        String token = UUID.randomUUID().toString();
//        Timestamp createdAt = new Timestamp((new Date()).getTime());
//        Timestamp expiresAt = new Timestamp((new Date(System.currentTimeMillis()+1000*60*60*24)).getTime());
        return new ClientConfirmationToken(token , client);//,createdAt,expiresAt,client);
    }
    public String ActivateAccount(String token){
        Optional<ClientConfirmationToken> clientConfirmationTokenOptional=this.tokenRepository.findByToken(token);
        if(clientConfirmationTokenOptional.isPresent()){
            Client client = this.clientRepository.findById(clientConfirmationTokenOptional.get().getClient().getId()).get();
            client.setIsActivated(true);
            this.clientRepository.save(client);
            this.tokenRepository.delete(clientConfirmationTokenOptional.get());
            return "Activated";
        }else return token +" : is a wrong token please try to re-subscribe";

    }

}
