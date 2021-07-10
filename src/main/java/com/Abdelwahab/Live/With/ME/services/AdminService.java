package com.Abdelwahab.Live.With.ME.services;

import com.Abdelwahab.Live.With.ME.dto.ArticleDTO;
import com.Abdelwahab.Live.With.ME.dto.ClientDTO;
import com.Abdelwahab.Live.With.ME.entities.Admin;
import com.Abdelwahab.Live.With.ME.entities.HelpMessage;
import com.Abdelwahab.Live.With.ME.entities.Manager;
import com.Abdelwahab.Live.With.ME.entities.SellerIDConfirmationRequest;
import com.Abdelwahab.Live.With.ME.enums.Role;
import com.Abdelwahab.Live.With.ME.repositories.AdminRepository;
import com.Abdelwahab.Live.With.ME.repositories.HelpMessageRepository;
import com.Abdelwahab.Live.With.ME.requests.ManagerRegisterRequest;
import com.Abdelwahab.Live.With.ME.services.email.MailService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@AllArgsConstructor
public class AdminService {
    private AdminRepository adminRepository;
    private HelpMessageRepository helpMessageRepository;
    private PasswordEncoder passwordEncoder;
    private ArticleService articleService;
    private ClientService clientService;
    private SellerIDConfirmationService sellerIDConfirmationService;
    private MailService mailService;

    public void initAdmin(String email, String password) {
        if (adminRepository.findByEmail(email).isPresent()) {
            System.out.println("admin already initialised");
            return;
        }
        Admin admin = new Admin();
        admin.setEmail("admin_" + email);
        admin.setPassword(this.passwordEncoder.encode(password));
        admin.setFirstName("Admin");
        admin.setLastName("");
        admin.setRole(Role.ADMIN);
        admin.setIsActivated(true);
        this.adminRepository.save(admin);
    }

    public List<HelpMessage> getAllMessages() {
        return this.helpMessageRepository.findAll();
    }

    public HelpMessage getMessage(long id) {
        try {
            if (this.helpMessageRepository.findById(id).isPresent())
                return this.helpMessageRepository.getOne(id);
            else return null;
        } catch (EntityNotFoundException e) {
            return null;
        }

    }

    public boolean deleteMessage(long id) {
        if (this.helpMessageRepository.existsById(id)) {
            this.helpMessageRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<ArticleDTO> getAllArticles() {
        return this.articleService.getAll();
    }

    public List<ArticleDTO> getAllClosed() {
        return this.articleService.getClosed();
    }

    public List<ArticleDTO> getAllDeleted() {
        return this.articleService.getDeleted();
    }

    public List<ClientDTO> getAllClients() {
        return this.clientService.getAll();
    }

    public List<SellerIDConfirmationRequest> getAllRequests() {
        return this.sellerIDConfirmationService.getAll();
    }

    public boolean acceptSeller(long clientId) {
        return this.sellerIDConfirmationService.acceptRequest(clientId);
    }

    public boolean rejectSeller(long clientId) {
        return this.sellerIDConfirmationService.rejectRequest(clientId);
    }

    public boolean deleteClient(long clientId) {
        return this.clientService.deleteClient(clientId);
    }

    public boolean deleteArticle(long articleId) {
        return this.articleService.deleteArticleByAdmin(articleId);
    }

    public SellerIDConfirmationRequest getOne(long requestId) {
        return this.sellerIDConfirmationService.getOne(requestId);
    }

    public boolean responseToMessage(long messageId, String context) {
        try {
            HelpMessage helpMessage = this.helpMessageRepository.getOne(messageId);
            this.mailService.send(helpMessage.getEmail(), helpMessage.getSubject(), this.buildEmail(context));
            return true;
        } catch (EntityNotFoundException e) {
            return false;
        }
    }

    private String buildEmail(String context) {
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
                "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">Live With ME</span>\n" +
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
                "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Hi ,</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> " + context + " </p> <p>See you soon</p>" +
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

}
