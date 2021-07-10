package com.Abdelwahab.Live.With.ME.baseEntities;

import com.Abdelwahab.Live.With.ME.entities.Client;

import javax.persistence.*;

@MappedSuperclass
public abstract class BaseReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    private Client reporter;
    private String contextOfReport;

}
