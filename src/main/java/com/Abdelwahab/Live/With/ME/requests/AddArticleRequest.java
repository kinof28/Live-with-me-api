package com.Abdelwahab.Live.With.ME.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddArticleRequest {

    private String title;
    private String country;
    private String state;
    private String province;
    private String address;
    private String description;
    private String type;
    private long price;
    private String priceType;
    private int numberOfRoomMates;
}
