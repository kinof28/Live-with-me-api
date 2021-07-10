package com.Abdelwahab.Live.With.ME.requests;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ManagerRegisterRequest {
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private String phoneNumber;
}
