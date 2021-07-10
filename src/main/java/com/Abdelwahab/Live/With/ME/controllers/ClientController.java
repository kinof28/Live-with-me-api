package com.Abdelwahab.Live.With.ME.controllers;
import com.Abdelwahab.Live.With.ME.dto.ArticleDTO;
import com.Abdelwahab.Live.With.ME.dto.ClientDTO;
import com.Abdelwahab.Live.With.ME.dto.DealDTO;
import com.Abdelwahab.Live.With.ME.dto.NotificationDTO;
import com.Abdelwahab.Live.With.ME.entities.Client;
import com.Abdelwahab.Live.With.ME.entities.Notification;
import com.Abdelwahab.Live.With.ME.exceptions.NonAuthorisedActionException;
import com.Abdelwahab.Live.With.ME.mappers.ArticleMapper;
import com.Abdelwahab.Live.With.ME.mappers.ClientMapper;
import com.Abdelwahab.Live.With.ME.requests.AddArticleRequest;
import com.Abdelwahab.Live.With.ME.requests.SellerRequest;
import com.Abdelwahab.Live.With.ME.requests.UpdateClientRequest;
import com.Abdelwahab.Live.With.ME.services.ArticleService;
import com.Abdelwahab.Live.With.ME.services.ClientService;
import com.Abdelwahab.Live.With.ME.services.SellerIDConfirmationService;
import lombok.AllArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.Optional;


@AllArgsConstructor
@RestController
@RequestMapping(path = "/api/v0/client")
@CrossOrigin(origins = "http://localhost:4200")
public class ClientController {
    private final ArticleService articleService;
    private final ClientService clientService;
    private final ClientMapper clientMapper;
    private final ArticleMapper articleMapper;
    private final SellerIDConfirmationService sellerIDConfirmationService;
    private final String profileImgStorage="/home/abdelwahab/Desktop/images/profiles";
    private final String articleImgStorage="/home/abdelwahab/Desktop/images/articles/";
    private final String requestsImgStorage="/home/abdelwahab/Desktop/images/requests/";
    @GetMapping("/")
    public ClientDTO getClientDto(@AuthenticationPrincipal Client client){
        return this.clientMapper.getDtO(this.clientService.getClient(client.getUsername()).get());
    }
    @GetMapping("/{id}")
    public ClientDTO getClientDtoById(@PathVariable long id){
        Optional<Client> clientO =this.clientService.getClient(id);
        if(clientO.isPresent())return this.clientMapper.getDtO(clientO.get());
        else return null;
    }
    @GetMapping("/myArticles")
    public List<ArticleDTO> getPublishedArticles(@AuthenticationPrincipal Client client){
        return this.articleMapper.getDtos(this.articleService.getClientArticle(client));
    }
    @GetMapping("/myClosedArticles")
    public List<ArticleDTO> getClosedArticles(@AuthenticationPrincipal Client client){
        return this.articleMapper.getDtos(this.articleService.getClientClosedArticle(client));
    }
    @GetMapping("/requestedArticles")
    public List<ArticleDTO> getRequestedArticles(@AuthenticationPrincipal Client client){
        return this.articleMapper.getDtos(this.articleService.getClientRequestedArticle(client));
    }
    @GetMapping("/deal/{articleId}")
    public DealDTO getDealByArticleId(@PathVariable long articleId){
        return this.articleService.getDealByArticleId(articleId);
    }
    @GetMapping("/interest/{articleId}")
    public void getInterested(@PathVariable long articleId,@AuthenticationPrincipal Client client){
        this.clientService.getInterested(client,articleId);
    }
    @GetMapping("/lost-interest/{articleId}")
    public void notInterested(@PathVariable long articleId,@AuthenticationPrincipal Client client){
        this.clientService.notInterested(client,articleId);
    }
    @GetMapping("/join/{articleId}")
    public String requestJoin(@PathVariable long articleId,@AuthenticationPrincipal Client client){
        this.clientService.requestJoin(articleId,client);
        return "\"Added\"";
    }
    @GetMapping("/accept_join/{articleId}/{clientId}")
    public String acceptJoinRequest(@PathVariable long articleId,@PathVariable long clientId,@AuthenticationPrincipal Client client){
        if(this.clientService.acceptJoinRequest(articleId,clientId,client)){
            return "\"approve done\"";
        }
        return "\"no thing yet done\"";
    }
    @GetMapping("/decline_join/{articleId}/{clientId}")
    public String declineJoinRequest(@PathVariable long articleId,@PathVariable long clientId,@AuthenticationPrincipal Client client){
        if(this.clientService.declineJoinRequest(articleId,clientId,client)){
            return "\"decline done\"";
        }
        return "\"no thing yet done\"";
    }
    @GetMapping("/omit/{dealId}/{articleId}/{clientId}")
    public String omitBuyerFromDeal(@PathVariable long dealId,@PathVariable long articleId,@PathVariable long clientId,@AuthenticationPrincipal Client client){
        if(this.clientService.omitBuyer(articleId,clientId,dealId,client)){
            return "\"Omited\"";
        }
        return "\"no omit yet done\"";
    }
    @GetMapping("/ignore/{notificationId}")
    public String ignoreNotification(@PathVariable  long notificationId,@AuthenticationPrincipal Client client){
            this.clientService.deleteNotification(notificationId);
            return "\"decline done\"";
    }
    @GetMapping("/notifications")
    public List<NotificationDTO> getNotifications(@AuthenticationPrincipal Client client){
        return this.clientService.getNotificationByClient(client);
    }
    @GetMapping("/cansubmit")
    public String canSubmit(@AuthenticationPrincipal Client client){
        if (this.sellerIDConfirmationService.canSubmit(client)) return "\"true\"";
        else return "\"false\"";
    }
    @PostMapping(value = "/profile/img/")
    public String updateProfileImg(@RequestParam("image") MultipartFile img,@AuthenticationPrincipal Client client){
        File path=new File(this.profileImgStorage);
        String newImg=client.getId()+"."+FilenameUtils.getExtension(img.getOriginalFilename());
        if(!path.exists()){
            path.mkdir();
        }
        if(client.getProfileImg()!=null){
            new File(this.profileImgStorage+File.separator+client.getProfileImg()).delete();
        }
        File serverFile = new File(path.getPath()+File.separator+newImg);
        try
        {
            FileUtils.writeByteArrayToFile(serverFile,img.getBytes());
            if(!this.clientService.updateProfileImg(client.getId(),newImg))throw new RuntimeException("some thing went wrong in updating profile image for client with id "+client.getId()) ;
        }catch(Exception e) {
            e.printStackTrace();
            return "\" error \"";
        }
        return "\" img add \"";
    }

    @PostMapping(value = "/seller/img/{id}")
    public String addSellerRequestImg(@RequestParam("image") MultipartFile img,@PathVariable long id,@AuthenticationPrincipal Client client){
        File path=new File(this.requestsImgStorage+File.separator+client.getId());
        String newImg=id+"."+FilenameUtils.getExtension(img.getOriginalFilename());
        if(!path.exists()){
            path.mkdir();
        }
        File serverFile = new File(path.getPath()+File.separator+newImg);
        try
        {
            FileUtils.writeByteArrayToFile(serverFile,img.getBytes());
        }catch(Exception e) {
            e.printStackTrace();
            return "\"error\"";
        }
        return "\"img:"+newImg+"\"";
    }
    @PostMapping(value = "/seller")
    public String submitSellerRequest(@RequestBody SellerRequest request ,@AuthenticationPrincipal Client client){
        this.sellerIDConfirmationService.submitRequest(request,client);
        return "\"submitted\"";
    }
    @PutMapping(value="/update")
    public String updateClientAccount(@RequestBody UpdateClientRequest request ,@AuthenticationPrincipal Client client){
        if(this.clientService.updateClient(request,client))return "\"updated\"";
        else return "\"error\"";
    }
    @PostMapping(value = "/add")
    public String addArticle(@RequestBody AddArticleRequest request, @AuthenticationPrincipal Client client){
        if(client.getIsSeller()){
            long id=this.articleService.addArticle(request,client);
            return "\"id:"+id+"\"";
        } else throw new NonAuthorisedActionException();
    }

    @PostMapping(value = "/article/img/{articleId}/{id}")
    public String addArticleImg(@RequestParam("image") MultipartFile img,@PathVariable long id,@PathVariable long articleId){
        File path=new File(this.articleImgStorage+File.separator+articleId);
        String newImg=id+"."+FilenameUtils.getExtension(img.getOriginalFilename());
        this.articleService.addImgToArticle(newImg,articleId);
        if(!path.exists()){
            path.mkdir();
        }
        File serverFile = new File(path.getPath()+File.separator+newImg);
        try
        {
            FileUtils.writeByteArrayToFile(serverFile,img.getBytes());
        }catch(Exception e) {
            e.printStackTrace();
            return "\"error\"";
        }
        return "\"img:"+newImg+"\"";
    }
    @GetMapping(value = "/article/main/{articleId}/{img}")
    public String makeImgMain(@PathVariable String img,@PathVariable long articleId){
        if(this.articleService.makeImgMain(img,articleId))return "\"set\"";
        else return "\"error\"";
    }
    @PutMapping(value = "/update/article")
    public String updateArticle(@RequestBody ArticleDTO request, @AuthenticationPrincipal Client client){
        if(client.getIsSeller()) {
            if (this.articleService.updateArticle(request, client)) {
                return "\"updated\"";
            } else return ("\"not updated\"");
        }return "\"non authorised action\"";
    }
    @DeleteMapping(value="/{articleId}")
    public String deleteArticle(@PathVariable long articleId,@AuthenticationPrincipal Client client){
        if(this.articleService.deleteArticle(articleId,client))return "\"deleted\"";
        return "\"no thing done yet\"";
    }
    @DeleteMapping(value="/{articleId}/{img}")
    public String deleteImgFromArticle(@PathVariable long articleId,@PathVariable String img,@AuthenticationPrincipal Client client){
        if(this.articleService.deleteImgFromArticle(articleId,img,client))return "\"deleted\"";
        return "\"no thing done yet\"";
    }
}
