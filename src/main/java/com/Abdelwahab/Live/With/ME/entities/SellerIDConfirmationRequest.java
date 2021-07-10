package com.Abdelwahab.Live.With.ME.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class SellerIDConfirmationRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    private Client client;
//    @OneToOne
//    private Manager manager;
    @Column(nullable = false)
    private Long idNumber;
    @Column(nullable = false)
    private String faceImage;
    @Column(nullable = false)
    private String idImage;
}
