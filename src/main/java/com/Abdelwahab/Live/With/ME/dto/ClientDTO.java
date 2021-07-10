package com.Abdelwahab.Live.With.ME.dto;

import com.Abdelwahab.Live.With.ME.entities.Deal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@AllArgsConstructor
@Getter
@Setter
public class ClientDTO {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String address;
    private String profileImg;
    private Boolean isSeller;
    private List<ArticleDTO> publishedArticles;
    private List<ArticleDTO> interestingArticles;
//    private List<DealDTO> deals;
}
