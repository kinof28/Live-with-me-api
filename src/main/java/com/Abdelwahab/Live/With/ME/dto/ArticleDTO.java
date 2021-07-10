package com.Abdelwahab.Live.With.ME.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ArticleDTO {
    private Long id;
    private Long publisherId;
    private String title;
    private String country;
    private String state;
    private String province;
    private String address;
    private String description;
    private String type;
    private Long price;
    private String priceType;
    private int numberOfRoomMates;
    private int remainingRoomMates;
    private String mainImage;
    private List<String> images;
}
