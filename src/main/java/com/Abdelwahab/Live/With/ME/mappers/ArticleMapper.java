package com.Abdelwahab.Live.With.ME.mappers;

import com.Abdelwahab.Live.With.ME.dto.ArticleDTO;
import com.Abdelwahab.Live.With.ME.entities.Article;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;
@Component
public class ArticleMapper {

    public ArticleDTO getDto(Article article){
        if(article!=null)
            return new ArticleDTO(article.getId(),
                article.getClient().getId(),
                article.getTitle(),
                article.getCountry(),
                article.getState(),
                article.getProvince(),
                article.getAddress(),
                article.getDescription(),
                article.getType(), article.getPrice(),
                article.getPriceType().name(),
                article.getNumberOfRoomMates(),
                article.getRemainingRoomMates(),
                article.getMainImage(),
                article.getImages());
        else return null;
    }

    public List<ArticleDTO> getDtos(List<Article> articles){

        LinkedList<ArticleDTO> dtos=new LinkedList<>();
        if(!articles.isEmpty())
        for(Article article: articles){
            dtos.add(this.getDto(article));
        }
        return dtos;
    }
}
