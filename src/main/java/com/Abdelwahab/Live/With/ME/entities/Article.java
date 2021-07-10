package com.Abdelwahab.Live.With.ME.entities;


import com.Abdelwahab.Live.With.ME.enums.PriceType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;


@Setter
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Article {
    @Id
@GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private Boolean isActive;
    @Column(nullable = false)
    private String country;
    @Column(nullable = false)
    private String state;
    private String province;
    @Column(nullable = false)
    private String address;
    @Column(columnDefinition = "TEXT")
    private String description;
    @Column(nullable = false)
    private String type;
    @Column(nullable = false)
    private Long price;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PriceType priceType;
    @Column(nullable = false)
    private int numberOfRoomMates;
    private int remainingRoomMates;
    @Column(name = "main_image")
    private String mainImage;
    @ElementCollection // 1
    @CollectionTable(name = "images", joinColumns = @JoinColumn(name = "id")) // 2
    @Column(name = "value")
    private List<String> images;
    @ManyToOne
    private Client client;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Article article = (Article) o;
        return id.equals(article.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
