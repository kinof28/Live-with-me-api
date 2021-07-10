package com.Abdelwahab.Live.With.ME.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class UpdateClientRequest {
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String address;
    private String newPassword;
}
