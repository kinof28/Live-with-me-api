package com.Abdelwahab.Live.With.ME.services;

import com.Abdelwahab.Live.With.ME.dto.ClientDTO;
import com.Abdelwahab.Live.With.ME.dto.NotificationDTO;
import com.Abdelwahab.Live.With.ME.entities.*;
import com.Abdelwahab.Live.With.ME.enums.NotificationType;
import com.Abdelwahab.Live.With.ME.enums.Role;
import com.Abdelwahab.Live.With.ME.exceptions.EmailAlreadyInUseException;
import com.Abdelwahab.Live.With.ME.mappers.ClientMapper;
import com.Abdelwahab.Live.With.ME.mappers.NotificationMapper;
import com.Abdelwahab.Live.With.ME.repositories.*;
import com.Abdelwahab.Live.With.ME.requests.ClientRegisterRequest;
import com.Abdelwahab.Live.With.ME.requests.UpdateClientRequest;
import com.Abdelwahab.Live.With.ME.services.email.MailSender;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.*;

@AllArgsConstructor
@Service
public class ClientService {
    private ClientRepository clientRepository;
    private ArticleRepository articleRepository;
    private ActivateClientAccountService activateService;
    private ClientConfirmationTokenRepository tokenRepository;
    private NotificationRepository notificationRepository;
    private DealRepository dealRepository;
    private PasswordEncoder passwordEncoder;
    private MailSender mailSender;
    private NotificationMapper notificationMapper;
    private ClientMapper clientMapper;

    public String registerClient(ClientRegisterRequest registerRequest) throws EmailAlreadyInUseException {
        if (this.clientRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            throw new EmailAlreadyInUseException();
        }
        Client client = new Client();
        client.setEmail(registerRequest.getEmail());
        client.setFirstName(registerRequest.getFirstName());
        client.setLastName(registerRequest.getLastName());
        client.setPassword(this.passwordEncoder.encode(registerRequest.getPassword()));
        client.setPhoneNumber(registerRequest.getPhoneNumber());
        client.setRole(Role.CLIENT);
        client.setIsSeller(false);
        client.setIsActivated(false);
        client.setAddress(null);
        client.setInterestingArticles(new ArrayList<>());
        client.setPublishedArticles(new ArrayList<>());
        ClientConfirmationToken token = this.activateService.createToken(client);
        this.clientRepository.save(client);
        this.tokenRepository.save(token);
        this.mailSender.send(client.getEmail(), this.buildEmail(client.getFirstName() + " " + client.getLastName(), "http://localhost:8080/api/v0/public/confirm/" + token.getToken()));
        return token.getToken();
    }

    public Optional<Client> getClient(long id) {
        return this.clientRepository.findById(id);
    }

    public Optional<Client> getClient(String email) {
        return this.clientRepository.findByEmail(email);
    }

    public boolean updateClient(Client client) {
        Optional<Client> clientO = this.clientRepository.findById(client.getId());
        if (clientO.isPresent()) {
            this.clientRepository.save(client);
            return true;
        }
        return false;
    }

    public boolean updateClient(UpdateClientRequest request, Client client) {
        if (request.getFirstName() != null && !request.getFirstName().equals("")) {
            client.setFirstName(request.getFirstName());
        }
        if (request.getLastName() != null && !request.getLastName().equals("")) {
            client.setLastName(request.getLastName());
        }
        if (request.getAddress() != null && !request.getAddress().equals("")) {

            client.setAddress(request.getAddress());
        }
        if (request.getPhoneNumber() != null && !request.getPhoneNumber().equals("")) {
            client.setPhoneNumber(request.getPhoneNumber());
        }
        if (request.getNewPassword() != null && !request.getNewPassword().equals("")) {
            client.setPassword(this.passwordEncoder.encode(request.getNewPassword()));
        }
        Optional<Client> clientO = this.clientRepository.findById(client.getId());
        if (clientO.isPresent()) {
            this.clientRepository.save(client);
            return true;
        }
        return false;
    }

    public boolean updateProfileImg(long id, String img) {
        Optional<Client> clientO = this.clientRepository.findById(id);
        if (clientO.isPresent()) {
            Client client = clientO.get();
            client.setProfileImg(img);
            this.clientRepository.save(client);
            return true;
        }
        return false;
    }

    public boolean makeClientSeller(long id) {
        Optional<Client> clientO = this.clientRepository.findById(id);
        if (clientO.isPresent()) {
            Client client = clientO.get();
            client.setIsSeller(true);
            return true;
        }
        return false;
    }
    public boolean addDeal(long articleID,Client client){
        Deal deal=new Deal();
        deal.setArticle(this.articleRepository.getOne(articleID));
        deal.setOpen(true);
        this.dealRepository.save(deal);
        return true;
    }

    public boolean publishArticle(Client client, long articleId) {
        List<Article> publishedArticles=new ArrayList<>(client.getPublishedArticles());
        publishedArticles.add(this.articleRepository.getOne(articleId));
        client.setPublishedArticles(publishedArticles);
        this.clientRepository.save(client);
        return true;
    }

    public boolean getInterested(Client client , long articleId) {
        List<Article> interestingArticles=client.getInterestingArticles();
        interestingArticles.add(this.articleRepository.getOne(articleId));
        client.setInterestingArticles(interestingArticles);
        this.clientRepository.save(client);
        return true;
    }
    public boolean notInterested(Client client , long articleId) {
        List<Article> interestingArticles=client.getInterestingArticles();
        if(interestingArticles.remove(this.articleRepository.getOne(articleId))){
            client.setInterestingArticles(interestingArticles);
            this.clientRepository.save(client);
            return true;
        }
        else return false;
    }
    public boolean requestJoin(long articleId,Client client){
        Article article=this.articleRepository.getOne(articleId);
        Optional<Deal> dealO=this.dealRepository.findByArticle(article);
        if(dealO.isPresent()){
            Notification notification=new Notification();
            notification.setClient(article.getClient());
            notification.setOrigin(client);
            notification.setType(NotificationType.JOINREQUEST);
            notification.setArticle(article);
            this.notificationRepository.save(notification);
            Deal deal=dealO.get();
            Collection<Client> requesters=deal.getRequesters();
            requesters.add(client);
            dealO.get().setRequesters(requesters);
            this.dealRepository.save(deal);

            return true;
        }
        return false;
    }
    public boolean acceptJoinRequest(long articleId,long clientID,Client client){

        try {
            Article article=this.articleRepository.getOne(articleId);
            Optional<Deal> dealO=this.dealRepository.findByArticle(article);
            if(dealO.isPresent()){
                Deal deal =dealO.get();
                if(client.equals(article.getClient())){
                    ArrayList<Client> requesters=new ArrayList<>(deal.getRequesters());
                    ArrayList<Client> buyers=new ArrayList<>(deal.getBuyers());
                    Client requester=this.clientRepository.getOne(clientID);
                    if(requesters.remove(requester)){
                        buyers.add(requester);
                        deal.setRequesters(requesters);
                        deal.setBuyers(buyers);
                        article.setRemainingRoomMates(article.getRemainingRoomMates()-1);
                        if(article.getRemainingRoomMates()==0){
                            deal.setOpen(false);
                            List<Article>published=client.getPublishedArticles();
                            published.remove(article);
                            client.setPublishedArticles(published);
                            this.clientRepository.save(client);
                        }
                        this.dealRepository.save(deal);
                        this.articleRepository.save(article);
                        Notification notification=new Notification();
                        notification.setClient(requester);
                        notification.setOrigin(client);
                        notification.setArticle(article);
                        notification.setType(NotificationType.JOINREQUESTAPPROAVAL);
                        this.notificationRepository.save(notification);
                        return true;
                    }else {
                        System.out.println("did not removed");
                        return false;
                    }
                }else {
                    System.out.println("client not matched");
                    return false;
                }
            }else {
                System.out.println("deal is not present");
                return false;
            }
        }catch(EntityNotFoundException e){
            System.out.println("some thing went wrong");
            return false;
        }
    }
    public boolean declineJoinRequest(long articleId,long clientID,Client client){
        try {
            Article article=this.articleRepository.getOne(articleId);
            Optional<Deal> dealO=this.dealRepository.findByArticle(article);
            if(dealO.isPresent()){
                Deal deal =dealO.get();
                if(client.equals(article.getClient())){
                    ArrayList<Client> requesters=new ArrayList<>(deal.getRequesters());
                    Client requester=this.clientRepository.getOne(clientID);
                    if(requesters.remove(requester)){
                        deal.setRequesters(requesters);
                        this.dealRepository.save(deal);
                        Notification notification=new Notification();
                        notification.setClient(requester);
                        notification.setOrigin(client);
                        notification.setArticle(article);
                        notification.setType(NotificationType.JOINREQUESTDECLINE);
                        this.notificationRepository.save(notification);
                        return true;
                    }else {
                        System.out.println("did not removed");
                        return false;
                    }
                }else {
                    System.out.println("client not matched");
                    return false;
                }
            }else {
                System.out.println("deal is not present");
                return false;
            }
        }catch(EntityNotFoundException e){
            System.out.println("some thing went wrong");

            return false;
        }
    }
    public boolean omitBuyer(long articleId,long clientID,long dealId,Client client){
        try {
            Article article=this.articleRepository.getOne(articleId);
            Deal deal=this.dealRepository.getOne(dealId);
                if(client.equals(article.getClient())){
                    ArrayList<Client> buyers=new ArrayList<>(deal.getBuyers());
                    Client requester=this.clientRepository.getOne(clientID);
                    if(buyers.remove(requester)){
                        deal.setBuyers(buyers);
                        if(!deal.getOpen()){
                            List<Article>published=client.getPublishedArticles();
                            published.add(article);
                            client.setPublishedArticles(published);
                        }
                        deal.setOpen(true);
                        this.clientRepository.save(client);
                        this.dealRepository.save(deal);
                        article.setRemainingRoomMates(article.getRemainingRoomMates()+1);
                        this.articleRepository.save(article);
                        Notification notification=new Notification();
                        notification.setClient(requester);
                        notification.setOrigin(client);
                        notification.setArticle(article);
                        notification.setType(NotificationType.OMITED);
                        this.notificationRepository.save(notification);
                        return true;
                    }else {
                        System.out.println("did not removed");
                        return false;
                    }
                }else {
                    System.out.println("client not matched");
                    return false;
                }
        }catch(EntityNotFoundException e){
            System.out.println("some thing went wrong");
            return false;
        }
    }
    public List<NotificationDTO> getNotificationByClient(Client client){
        return this.notificationMapper.getDtos(this.notificationRepository.findByClient(client));
    }
    public void deleteNotification(long notificationId){
        this.notificationRepository.deleteById(notificationId);
    }

    public List<ClientDTO> getAll(){
        return this.clientMapper.getsDtos(this.clientRepository.findAllByIsActivatedIsTrue());
    }
    public boolean deleteClient(long clientId){
        Optional<Client> clientO=this.clientRepository.findById(clientId);
        if(clientO.isPresent()){
            clientO.get().setIsActivated(false);
            this.clientRepository.save(clientO.get());
            return true;
        }else return false;
    }
    public boolean resetPassword(String email){
        Optional<Client> clientO=this.getClient(email);
        if(clientO.isPresent()){
            Client client=clientO.get();
            if(client.getIsActivated()){
                String newPassword=this.generatePassword();
                client.setPassword(this.passwordEncoder.encode(newPassword));
                this.clientRepository.save(client);
                this.mailSender.send(client.getEmail(),"Reset Your Password", this.buildResetEmail(client.getFirstName() + " " + client.getLastName(),newPassword));
                return true;
            }
        }
        return false;
    }
    private String buildEmail(String name, String link) {
        return "<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c\">\n" +
                "\n" +
                "<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n" +
                "\n" +
                "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"100%\" height=\"53\" bgcolor=\"#0b0c0c\">\n" +
                "        \n" +
                "        <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
                "          <tbody><tr>\n" +
                "            <td width=\"70\" bgcolor=\"#0b0c0c\" valign=\"middle\">\n" +
                "                <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td style=\"padding-left:10px\">\n" +
                "                  \n" +
                "                    </td>\n" +
                "                    <td style=\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\">\n" +
                "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">Confirm your email</span>\n" +
                "                    </td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "              </a>\n" +
                "            </td>\n" +
                "          </tr>\n" +
                "        </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"10\" height=\"10\" valign=\"middle\"></td>\n" +
                "      <td>\n" +
                "        \n" +
                "                <table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td bgcolor=\"#1D70B8\" width=\"100%\" height=\"10\"></td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\" height=\"10\"></td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "\n" +
                "\n" +
                "\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "      <td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max-width:560px\">\n" +
                "        \n" +
                "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Hi " + name + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Thank you for registering. Please click on the below link to activate your account: </p><blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\"><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> <a href=\"" + link + "\">Activate Now</a> </p></blockquote> <p>See you soon</p>" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "  </tbody></table><div class=\"yj6qo\"></div><div class=\"adL\">\n" +
                "\n" +
                "</div></div>";
    }
    private String buildResetEmail(String name, String password) {
        return "<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c\">\n" +
                "\n" +
                "<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n" +
                "\n" +
                "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"100%\" height=\"53\" bgcolor=\"#0b0c0c\">\n" +
                "        \n" +
                "        <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
                "          <tbody><tr>\n" +
                "            <td width=\"70\" bgcolor=\"#0b0c0c\" valign=\"middle\">\n" +
                "                <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td style=\"padding-left:10px\">\n" +
                "                  \n" +
                "                    </td>\n" +
                "                    <td style=\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\">\n" +
                "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">Reset Your Password</span>\n" +
                "                    </td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "              </a>\n" +
                "            </td>\n" +
                "          </tr>\n" +
                "        </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"10\" height=\"10\" valign=\"middle\"></td>\n" +
                "      <td>\n" +
                "        \n" +
                "                <table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td bgcolor=\"#1D70B8\" width=\"100%\" height=\"10\"></td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\" height=\"10\"></td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "\n" +
                "\n" +
                "\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "      <td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max-width:560px\">\n" +
                "        \n" +
                "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Hi " + name + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Use this password to login and change it later : </p><blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\"><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> " + password + "</p></blockquote> <p>See you soon</p>" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "  </tbody></table><div class=\"yj6qo\"></div><div class=\"adL\">\n" +
                "\n" +
                "</div></div>";
    }
    private String generatePassword(){
        String password="";
        for(int i=0;i<8;i++){
            password=password+this.randomCharacter();
        }
        return password;
    }
    private char randomCharacter() {
        int rand = (int) (Math.random() * 62);
        if (rand <= 9) {
            int number = rand + 48;
            return (char) (number);
        } else if (rand <= 35) {
            int uppercase = rand + 55;
            return (char) (uppercase);
        } else {
            int lowercase = rand + 61;
            return (char) (lowercase);
        }
    }
}
