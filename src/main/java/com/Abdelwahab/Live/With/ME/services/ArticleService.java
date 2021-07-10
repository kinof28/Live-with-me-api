package com.Abdelwahab.Live.With.ME.services;

import com.Abdelwahab.Live.With.ME.dto.ArticleDTO;
import com.Abdelwahab.Live.With.ME.dto.DealDTO;
import com.Abdelwahab.Live.With.ME.entities.Article;
import com.Abdelwahab.Live.With.ME.entities.Client;
import com.Abdelwahab.Live.With.ME.entities.Deal;
import com.Abdelwahab.Live.With.ME.enums.PriceType;
import com.Abdelwahab.Live.With.ME.mappers.ArticleMapper;
import com.Abdelwahab.Live.With.ME.mappers.DealMapper;
import com.Abdelwahab.Live.With.ME.repositories.ArticleReportRepository;
import com.Abdelwahab.Live.With.ME.repositories.ArticleRepository;
import com.Abdelwahab.Live.With.ME.repositories.DealRepository;
import com.Abdelwahab.Live.With.ME.requests.AddArticleRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.*;

@Service
@AllArgsConstructor
public class ArticleService {
    private ArticleRepository articleRepository;
    private ClientService clientService;
    private ArticleMapper articleMapper;
    private DealRepository dealRepository;
    private DealMapper dealMapper;

    public long addArticle(Client client, Article article) {
        long id = (this.articleRepository.save(article)).getId();
        this.clientService.publishArticle(client, id);
        this.clientService.addDeal(id, client);
        return id;
    }

    public long addArticle(AddArticleRequest request, Client client) {
        Article article = new Article();
        article.setTitle(request.getTitle());
        article.setIsActive(true);
        article.setCountry(request.getCountry());
        article.setState(request.getState());
        article.setProvince(request.getProvince());
        article.setAddress(request.getAddress());
        article.setDescription(request.getDescription());
        article.setType((request.getType()));
        article.setPrice(request.getPrice());
        article.setPriceType(PriceType.valueOf(request.getPriceType()));
        article.setNumberOfRoomMates(request.getNumberOfRoomMates());
        article.setRemainingRoomMates(request.getNumberOfRoomMates());
        article.setClient(client);
        return this.addArticle(client, article);
    }
    public boolean updateArticle(ArticleDTO request, Client client) {
        try{
            Article article = this.articleRepository.getOne(request.getId());
            if(!article.getClient().equals(client)) return false;
            article.setTitle(request.getTitle());
//            article.setIsActive(true);
            article.setCountry(request.getCountry());
            article.setState(request.getState());
            article.setProvince(request.getProvince());
            article.setAddress(request.getAddress());
            article.setDescription(request.getDescription());
            article.setType((request.getType()));
            article.setPrice(request.getPrice());
            article.setPriceType(PriceType.valueOf(request.getPriceType()));
            article.setNumberOfRoomMates(request.getNumberOfRoomMates());
            article.setRemainingRoomMates(request.getRemainingRoomMates());
            this.articleRepository.save(article);
            return true;
        }catch(EntityNotFoundException e){
            System.out.println("some thing went wrong in updating article");
            return false;
        }

    }

    public boolean addImgToArticle(String img, long articleId) {
        Optional<Article> articleO = this.articleRepository.findById(articleId);
        if (articleO.isPresent()) {
            Article article = articleO.get();
            List<String> images = article.getImages();
            images.add(img);
            article.setImages(images);
            this.articleRepository.save(article);
            return true;
        }
        return false;
    }

    public boolean makeImgMain(String img, long articleId) {
        Optional<Article> articleO = this.articleRepository.findById(articleId);
        if (articleO.isPresent()) {
            Article article = articleO.get();
            article.setMainImage(img);
            this.articleRepository.save(article);
            return true;
        }
        return false;
    }

//    public boolean deleteArticle(Article article) {
//
//        if (this.articleRepository.existsById(article.getId())) {
//            this.articleRepository.deleteById(article.getId());
//        }
//        return false;
//    }

    public boolean deleteArticle(long id,Client client) {
        try{
            Article article=this.articleRepository.getOne(id);
            if(!article.getClient().equals(client))return false;
            article.setIsActive(false);
            this.articleRepository.save(article);
            List<Article> published=new ArrayList<>(client.getPublishedArticles());
            if(published.remove(article)){
                client.setPublishedArticles(published);
                if(this.clientService.updateClient(client)){
                    return true;
                }return false;
            }return false;
        }catch(EntityNotFoundException e){
            System.out.println("some thing went wrong in deleting article");
            return false;
        }
    }
    public boolean deleteArticleByAdmin(long id) {
        try{

            Article article=this.articleRepository.getOne(id);
            Client client=article.getClient();
//            if(!article.getClient().equals(client))return false;
            article.setIsActive(false);
            this.articleRepository.save(article);
            List<Article> published=new ArrayList<>(client.getPublishedArticles());
            if(published.remove(article)){
                client.setPublishedArticles(published);
                if(this.clientService.updateClient(client)){
                    return true;
                }return false;
            }return false;
        }catch(EntityNotFoundException e){
            System.out.println("some thing went wrong in deleting article By admin");
            return false;
        }
    }
    public boolean deleteImgFromArticle(long id,String img,Client client){
        try{
            Article article=this.articleRepository.getOne(id);
            if(!article.getClient().equals(client))return false;
            List<String> images=article.getImages();
            if(images.remove(img)){
                article.setImages(images);
                this.articleRepository.save(article);
                return true;
            }else return false;
        }catch(EntityNotFoundException e){
            System.out.println("some thing went wrong in deleting image from article");
            return false;
        }
    }

    public boolean updateArticle(Article article) {
        if (this.articleRepository.existsById(article.getId())) {
            this.articleRepository.save(article);
            return true;
        }
        return false;
    }

    public Article getArticle(long id) {
        try{
            Article article=this.articleRepository.getOne(id);
            if(article.getIsActive())
                return article;
            else return null;
        }catch(EntityNotFoundException e){
            return null;
        }

    }
    public ArticleDTO getArticleForAdmin(long id) {
        try{
            Article article=this.articleRepository.getOne(id);
                   return this.articleMapper.getDto(article);
            }catch(EntityNotFoundException e){
            return null;
        }

    }
    public Article getArticleG(long id) {
        try{
            return this.articleRepository.getOne(id);

            }catch(EntityNotFoundException e){
            return null;
        }

    }


    public DealDTO getDealByArticleId(long articleId) {
        try {
            Article article = this.articleRepository.getOne(articleId);
            Optional<Deal> dealO = this.dealRepository.findByArticle(article);
            if (dealO.isEmpty()) throw new EntityNotFoundException();
            return this.dealMapper.getDto(dealO.get());
        } catch (EntityNotFoundException e) {
            return null;
        }
    }

    public List<Article> getClientArticle(Client client) {
        return this.articleRepository.findByClient(client);
    }

    public List<Article> getClientClosedArticle(Client client) {
        return this.articleRepository.findClosedByClient(client);
    }

    public List<Article> getClientRequestedArticle(Client client) {
        List<Article> articles = new ArrayList<>();

        for (Deal d : this.dealRepository.findByRequestersContains(client)) {
            articles.add(d.getArticle());
        }
        return articles;
    }


    public Map<String, List<ArticleDTO>> search(String text) {
        HashMap<String, List<ArticleDTO>> result = new HashMap<>();
        result.put("title", this.articleMapper.getDtos(this.articleRepository.findByTitleLikeIgnoreCase("%"+text.toUpperCase()+"%")));
        result.put("country", this.articleMapper.getDtos(this.articleRepository.findByCountryLikeIgnoreCase("%"+text.toUpperCase()+"%")));
        result.put("state", this.articleMapper.getDtos(this.articleRepository.findByStateLikeIgnoreCase("%"+text.toUpperCase()+"%")));
        result.put("province", this.articleMapper.getDtos(this.articleRepository.findByProvinceLikeIgnoreCase("%"+text.toUpperCase()+"%")));
        result.put("description", this.articleMapper.getDtos(this.articleRepository.findByDescriptionLikeIgnoreCase("%"+text.toUpperCase()+"%")));
        result.put("type", this.articleMapper.getDtos(this.articleRepository.findByTypeLikeIgnoreCase("%"+text.toUpperCase()+"%")));
        return result;
    }

    public Map<String, List<ArticleDTO>> explore(String city, String country) {
        HashMap<String, List<ArticleDTO>> result = new HashMap<>();
        result.put("country", this.articleMapper.getDtos(this.articleRepository.findByCountryLikeIgnoreCase("%"+country.toUpperCase()+"%")));
        result.put("state", this.articleMapper.getDtos(this.articleRepository.findByStateLikeIgnoreCase("%"+city.toUpperCase()+"%")));
        result.put("province", this.articleMapper.getDtos(this.articleRepository.findByProvinceLikeIgnoreCase("%"+city.toUpperCase()+"%")));
        return result;
    }

    public List<ArticleDTO> getAll() {
        return this.articleMapper.getDtos(this.articleRepository.findAll());
    }
    public List<ArticleDTO> getClosed() {
        return this.articleMapper.getDtos(this.articleRepository.findAllClosed());
    }
    public List<ArticleDTO> getDeleted() {
        return this.articleMapper.getDtos(this.articleRepository.findAllDeleted());
    }

    public ArticleDTO getOne(long id) {
        try{
                   return this.articleMapper.getDto(this.articleRepository.getOneById(id));
        }catch(EntityNotFoundException e){
            return null;
        }
    }


}
