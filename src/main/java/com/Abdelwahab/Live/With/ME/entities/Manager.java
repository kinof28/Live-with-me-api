package com.Abdelwahab.Live.With.ME.entities;

import com.Abdelwahab.Live.With.ME.baseEntities.BaseUser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.Collection;
import java.util.List;

@Setter
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Manager extends BaseUser {
    @OneToMany
    private List<ClientReport> reportedClients;
    @OneToMany
    private List<ArticleReport> reportedArticles;
    @OneToMany
    private List<SellerIDConfirmationRequest> confirmationsDemands;

}
