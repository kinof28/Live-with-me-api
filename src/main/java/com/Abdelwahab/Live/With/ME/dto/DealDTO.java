package com.Abdelwahab.Live.With.ME.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@AllArgsConstructor
@Setter
@Getter
public class DealDTO {
    private Long id;
    private Boolean open;
    private ArticleDTO article;
    private List<ClientDTO> requesters;
    private List<ClientDTO> buyers;


}
