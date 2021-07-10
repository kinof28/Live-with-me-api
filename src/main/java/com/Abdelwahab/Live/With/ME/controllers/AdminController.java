package com.Abdelwahab.Live.With.ME.controllers;

import com.Abdelwahab.Live.With.ME.dto.ArticleDTO;
import com.Abdelwahab.Live.With.ME.dto.ClientDTO;
import com.Abdelwahab.Live.With.ME.entities.Article;
import com.Abdelwahab.Live.With.ME.entities.HelpMessage;
import com.Abdelwahab.Live.With.ME.entities.SellerIDConfirmationRequest;
import com.Abdelwahab.Live.With.ME.services.AdminService;
import com.Abdelwahab.Live.With.ME.services.ArticleService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping(path = "/api/v0/admin")
@CrossOrigin(origins = "http://localhost:4401")
public class AdminController {
    private AdminService adminService;
    private ArticleService articleService;

    @GetMapping(value = "/activeArticles")
    public List<ArticleDTO> getActiveArticles()
    {
        return this.adminService.getAllArticles();
    }
    @GetMapping(value = "/closedArticles")
    public List<ArticleDTO> getClosedArticles()
    {
        return this.adminService.getAllClosed();
    }
    @GetMapping(value = "/deletedArticles")
    public List<ArticleDTO> getDeletedArticles()
    {
        return this.adminService.getAllDeleted();
    }
    @GetMapping(value = "/clients")
    public List<ClientDTO> getAllClients()
    {
        return this.adminService.getAllClients();
    }
    @GetMapping(value = "/messages")
    public List<HelpMessage> getAllMessages()
    {
        return this.adminService.getAllMessages();
    }
    @GetMapping(value = "/message/{id}")
    public HelpMessage getMessage(@PathVariable long id)
    {
        return this.adminService.getMessage(id);
    }
    @GetMapping(value = "/requests")
    public List<SellerIDConfirmationRequest> getAllRequests()
    {
        return this.adminService.getAllRequests();
    }
    @GetMapping(value = "/request/{id}")
    public SellerIDConfirmationRequest getRequest(@PathVariable long id)
    {
        return this.adminService.getOne(id);
    }
    @GetMapping(value = "/approve/request/{id}")
    public String approveRequest(@PathVariable long id)
    {
        return this.adminService.acceptSeller(id)?"\"accepted\"":"\"error\"";
    }
    @GetMapping(value = "/decline/request/{id}")
    public String declineRequest(@PathVariable long id)
    {
        return this.adminService.rejectSeller(id)?"\"accepted\"":"\"error\"";
    }
    @GetMapping(value = "/article/{id}")
    private ArticleDTO getArticle(@PathVariable long id){
        return this.articleService.getArticleForAdmin(id);
    }

    @PostMapping("message/{messageId}")
    public String responseMessage(@PathVariable long messageId,@RequestBody String context){
        if(this.adminService.responseToMessage(messageId,context)){
            return "\"responded\"";
        }
        else return"\"error\"";
    }
    @DeleteMapping("deleteClient/{clientId}")
    public String deleteClient(@PathVariable long clientId){
        if(this.adminService.deleteClient(clientId)){
            return "\"deleted\"";
        }
        else return"\"error\"";
    }
    @DeleteMapping("deleteArticle/{articleId}")
    public String deleteArticle(@PathVariable long articleId){
        if(this.adminService.deleteArticle(articleId)){
            return "\"deleted\"";
        }
        else return"\"error\"";
    }
    @DeleteMapping("deleteMessage/{messageId}")
    public String deleteMessage(@PathVariable long messageId){
        if(this.adminService.deleteMessage(messageId)){
            return "\"deleted\"";
        }
        else return"\"error\"";
    }
    public void UpdateAdminDetails(){

    }

}
