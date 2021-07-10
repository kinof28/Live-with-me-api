package com.Abdelwahab.Live.With.ME.services;

import com.Abdelwahab.Live.With.ME.entities.Client;
import com.Abdelwahab.Live.With.ME.entities.Notification;
import com.Abdelwahab.Live.With.ME.entities.SellerIDConfirmationRequest;
import com.Abdelwahab.Live.With.ME.enums.NotificationType;
import com.Abdelwahab.Live.With.ME.repositories.ClientRepository;
import com.Abdelwahab.Live.With.ME.repositories.NotificationRepository;
import com.Abdelwahab.Live.With.ME.repositories.SellerIDConfirmationRepository;
import com.Abdelwahab.Live.With.ME.requests.SellerRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class SellerIDConfirmationService {
    private SellerIDConfirmationRepository sellerIDConfirmationRepository;
    private ClientRepository clientRepository;
    private NotificationRepository notificationRepository;

    public SellerIDConfirmationRequest getOne(long id){
        if(this.sellerIDConfirmationRepository.findById(id).isPresent())
            return this.sellerIDConfirmationRepository.findById(id).get();
        else return null;
    }
    public boolean canSubmit(Client client){
        return !this.sellerIDConfirmationRepository.findByClient(client).isPresent();
    }
    public void submitRequest(SellerRequest request,Client client){
        SellerIDConfirmationRequest confirmationRequest=new SellerIDConfirmationRequest();
        confirmationRequest.setClient(client);
        confirmationRequest.setIdNumber(request.getIdNumber());
        confirmationRequest.setFaceImage(request.getFaceImg());
        confirmationRequest.setIdImage(request.getIdImg());
        this.sellerIDConfirmationRepository.save(confirmationRequest);
    }
    public boolean acceptRequest(long clientId){
        try{
            Client client=this.clientRepository.getOne(clientId);
            Optional<SellerIDConfirmationRequest> requestO= this.sellerIDConfirmationRepository.findByClient(client);
            if(requestO.isPresent()){
                client.setIsSeller(true);
                this.clientRepository.save(client);
                Notification notification=new Notification();
                notification.setClient(client);
                notification.setType(NotificationType.DEMANDEAPPROAVAL);
                this.notificationRepository.save(notification);
                this.sellerIDConfirmationRepository.delete(requestO.get());
                return true;
            }else return false;
        }catch(EntityNotFoundException e){
            return false;
        }

    }
    public boolean rejectRequest(long clientId){
        try{
            Client client=this.clientRepository.getOne(clientId);
            Optional<SellerIDConfirmationRequest> requestO= this.sellerIDConfirmationRepository.findByClient(client);
            if(requestO.isPresent()){
                this.sellerIDConfirmationRepository.delete(requestO.get());
                Notification notification=new Notification();
                notification.setClient(client);
                notification.setType(NotificationType.DEMANDEDECLINE);
                this.notificationRepository.save(notification);
                return true;
            }else return false;
        }catch(EntityNotFoundException e){
            return false;
        }
    }
    public List<SellerIDConfirmationRequest> getAll(){
        return this.sellerIDConfirmationRepository.findAll();
    }
}
