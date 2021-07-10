package com.Abdelwahab.Live.With.ME.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ClientRegisterRequest {

    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private String phoneNumber;

}
