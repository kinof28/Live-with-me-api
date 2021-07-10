package com.Abdelwahab.Live.With.ME.repositories;

import com.Abdelwahab.Live.With.ME.entities.Article;
import com.Abdelwahab.Live.With.ME.entities.Client;
import com.Abdelwahab.Live.With.ME.entities.Deal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DealRepository  extends JpaRepository<Deal,Long> {
    Optional<Deal> findByArticle(Article article);
    List<Deal> findByRequestersContains(Client client);
}
