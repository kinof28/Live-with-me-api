package com.Abdelwahab.Live.With.ME.controllers;

import com.Abdelwahab.Live.With.ME.dto.ArticleDTO;
import com.Abdelwahab.Live.With.ME.entities.Article;
import com.Abdelwahab.Live.With.ME.entities.Client;
import com.Abdelwahab.Live.With.ME.entities.HelpMessage;
import com.Abdelwahab.Live.With.ME.exceptions.EmailAlreadyInUseException;
import com.Abdelwahab.Live.With.ME.requests.ClientRegisterRequest;
import com.Abdelwahab.Live.With.ME.requests.LoginRequest;
import com.Abdelwahab.Live.With.ME.services.ActivateClientAccountService;
import com.Abdelwahab.Live.With.ME.services.ArticleService;
import com.Abdelwahab.Live.With.ME.services.ClientService;
import com.Abdelwahab.Live.With.ME.services.HelpService;
import com.Abdelwahab.Live.With.ME.utilities.JWTUtility;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping(path = "api/v0/public")
@CrossOrigin(origins = "http://localhost:4200")
public class PublicController {

    private ArticleService articleService;
    private ClientService clientService;
    private AuthenticationManager authenticationManager;
    private JWTUtility jwtUtility;
    private ActivateClientAccountService activateClientAccountService;
    private HelpService helpService;
    private final String profileImgStorage="/home/abdelwahab/Desktop/images/profiles";
    private final String articleImgStorage="/home/abdelwahab/Desktop/images/articles";
    private final String requestsImgStorage="/home/abdelwahab/Desktop/images/requests";

    @GetMapping(value = "/articles")
    private List<ArticleDTO> getAll(){
        return this.articleService.getAll();
    }

    @GetMapping(value = "/article/{id}")
    private ArticleDTO getArticleById(@PathVariable long id ){
        try{
            return this.articleService.getOne(id);
        }
        catch (EntityNotFoundException e){
            return null;
        }
    }

    @GetMapping(value = "/")
    private String home() {
            return "hello";
        }

    @GetMapping(value = "/search/{text}")
    private Map<String,List<ArticleDTO>> find(@PathVariable String text){

        return this.articleService.search(text);
    }
    @GetMapping(value = "/explore/{city}/{country}")
    private Map<String,List<ArticleDTO>> explore(@PathVariable String city,@PathVariable String country){

        return this.articleService.explore(city,country);
    }
    @GetMapping("/img/{id}")
    public byte[] getProfilImg(@PathVariable long id){
        try{
            Optional<Client> clientO=this.clientService.getClient(id);
            if(clientO.isPresent()&&clientO.get().getProfileImg()!=null&&clientO.get().getProfileImg()!=""){
                return Files.readAllBytes(Paths.get(this.profileImgStorage+File.separator+clientO.get().getProfileImg()));
            }else
                return Files.readAllBytes(Paths.get(this.profileImgStorage+File.separator+"avatar7.png"));
        }catch(IOException e){
            e.printStackTrace();
            return null;
        }
    }
    @GetMapping("/img/{articleId}/{img}")
    public byte[] getArticleImg(@PathVariable String img,@PathVariable long articleId){
        try{
            Article article=this.articleService.getArticleG(articleId);
            if(article!=null&&article.getImages().contains(img)){
                return Files.readAllBytes(Paths.get(this.articleImgStorage+File.separator+articleId+File.separator+img));
            }else {
                return Files.readAllBytes(Paths.get(this.articleImgStorage+File.separator+"default.png"));
            }
        }catch(IOException e){
            e.printStackTrace();
            return null;
        }
    }
    @GetMapping("/reqimg/{clientId}/{img}")
    public byte[] getRequestImg(@PathVariable String img,@PathVariable long clientId){
        try{
            return Files.readAllBytes(Paths.get(this.requestsImgStorage+ File.separator+clientId+File.separator+img));

        }catch(IOException e){
            e.printStackTrace();
            return null;
        }
    }
    @GetMapping(value = "/articles/{id}")
    private ResponseEntity<Article> getArticle(@PathVariable long id){
        Article articleO= this.articleService.getArticle(id);
        if(articleO != null) return new ResponseEntity<>(articleO, HttpStatus.OK);//articleO.get();
        else return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @GetMapping(value="/confirm/{token}")
    private String confirmClient(@PathVariable String token){
        return this.activateClientAccountService.ActivateAccount(token);
    }
    @GetMapping(value="/reset/{email}")
    private String resetPassword(@PathVariable String email){
        if(this.clientService.resetPassword(email))
        return "\"reset : "+email+" \"";
        else return "\" error \"";
    }
    @PostMapping("/login")
    public String login(@RequestBody LoginRequest request){
        try{

            this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUserName(),request.getPassword()));

        }catch(AuthenticationException e ){
            return "\" bad Credentials \"";
        }
        return "\"Bearer "+this.jwtUtility.generateToken(request.getUserName())+"\"";
    }
    @PostMapping("/live-with-me-head-master")
    public String adminLogin(@RequestBody LoginRequest request){
        try{

            this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(("admin_"+request.getUserName()),request.getPassword()));

        }catch(AuthenticationException e ){
            return "\" bad Credentials \"";
        }
        return "\"Bearer "+this.jwtUtility.generateToken(request.getUserName())+"\"";
    }


    @PostMapping(value="/subscribe")
    private String subscribeClient(@RequestBody ClientRegisterRequest request){
        try{
            String token =this.clientService.registerClient(request);
            return "\"subscribed ...."+token+"\"";
        }catch(EmailAlreadyInUseException e){
            return "\""+e+" ...... please try again \"";
        }

    }
    @PostMapping(value="/help")
    private String SubmitHelpRequest(@RequestBody HelpMessage helpMessage){
            this.helpService.submitHelpRequest(helpMessage);
            return "\"submiteded ....\"";
    }
}
