package com.Abdelwahab.Live.With.ME.services;

import com.Abdelwahab.Live.With.ME.baseEntities.BaseReport;
import com.Abdelwahab.Live.With.ME.entities.*;
import com.Abdelwahab.Live.With.ME.enums.Role;
import com.Abdelwahab.Live.With.ME.exceptions.EmailAlreadyInUseException;
import com.Abdelwahab.Live.With.ME.repositories.ManagerRepository;
import com.Abdelwahab.Live.With.ME.requests.ManagerRegisterRequest;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ManagerService {
    private ManagerRepository managerRepository;
    private PasswordEncoder passwordEncoder;
    public void registerManager(Manager manager)throws EmailAlreadyInUseException{
        if(this.managerRepository.findByEmail(manager.getEmail()).isPresent()){
            throw new EmailAlreadyInUseException();
        }
        this.managerRepository.save(manager);
    }
    public void registerManager(ManagerRegisterRequest registerRequest) throws EmailAlreadyInUseException{
        if(this.managerRepository.findByEmail(registerRequest.getEmail()).isPresent()){
            throw new EmailAlreadyInUseException();
        }
        Manager manager = new Manager();
        manager.setEmail(registerRequest.getEmail());
        manager.setFirstName(registerRequest.getFirstName());
        manager.setLastName(registerRequest.getLastName());
        manager.setPassword(this.passwordEncoder.encode(registerRequest.getPassword()));
        manager.setPhoneNumber(registerRequest.getPhoneNumber());
        manager.setRole(Role.MANAGER);
        manager.setIsActivated(false);
        manager.setReportedClients(new ArrayList<ClientReport>());
        manager.setReportedArticles(new ArrayList<ArticleReport>());
        manager.setConfirmationsDemands(new ArrayList<SellerIDConfirmationRequest>());
        this.managerRepository.save(manager);

    }
    public Optional<Manager> getManager(long id){
        return this.managerRepository.findById(id);
    }
    public Optional<Manager> getManager(String email){
        return this.managerRepository.findByEmail(email);
    }

    public boolean updateManager(Manager manager){
        if (this.getManager(manager.getId()).isPresent()){
            this.managerRepository.save(manager);
            return true;
        }
        return false;
    }
    public boolean activateManager(long id){
        Optional<Manager> managerO=this.getManager(id);
        if (managerO.isPresent()){
            Manager manager=managerO.get();
            manager.setIsActivated(true);
            return true;
        }
        return false;
    }
    public boolean assignReport(long id, BaseReport report){
        Optional<Manager> managerO=this.getManager(id);
        if (managerO.isPresent()){
            Manager manager=managerO.get();
            if(report instanceof ArticleReport){
                manager.getReportedArticles().add((ArticleReport) report);
            }else {
                manager.getReportedClients().add((ClientReport) report);
            }
            return true;
        }
        return false;
    }
    public boolean assignSellerIDConfirmation(long id, SellerIDConfirmationRequest confirmationRequest){

        Optional<Manager> managerO=this.getManager(id);
        if (managerO.isPresent()){
            Manager manager=managerO.get();
            manager.getConfirmationsDemands().add(confirmationRequest);
            return true;
        }
        return false;
    }
    public boolean deleteManager(long id){
        if(this.managerRepository.findById(id).isPresent()){
            this.managerRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
