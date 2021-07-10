package com.Abdelwahab.Live.With.ME.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class SellerRequest {
    @NonNull
    private long idNumber;
    @NonNull
    private String faceImg;
    @NonNull
    private String idImg;

}
