package com.Abdelwahab.Live.With.ME.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Setter
@Getter
public class HelpMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String email;
    private String name;
    @Column(columnDefinition = "Text")
    private String subject;
    @Column(columnDefinition = "LongText")
    private String context;
}
