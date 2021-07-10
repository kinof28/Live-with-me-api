package com.Abdelwahab.Live.With.ME.entities;

import com.Abdelwahab.Live.With.ME.baseEntities.BaseReport;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
@AllArgsConstructor
@NoArgsConstructor
public class ArticleReport extends BaseReport {
    @OneToOne
    private Article reportedArticle;
}
