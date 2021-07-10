package com.Abdelwahab.Live.With.ME.entities;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Deal {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Boolean open;
    @OneToOne
    private Article article;
    @ManyToMany//(fetch = FetchType.EAGER)
//    @LazyCollection(LazyCollectionOption.FALSE)
    private Collection<Client> requesters=new ArrayList<>();
    @ManyToMany
    private Collection<Client> buyers=new ArrayList<>();

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        Deal deal = (Deal) o;
//        return Objects.equals(id, deal.id);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(id);
//    }
}
