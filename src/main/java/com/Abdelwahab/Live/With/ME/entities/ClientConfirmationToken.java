package com.Abdelwahab.Live.With.ME.entities;

import com.Abdelwahab.Live.With.ME.baseEntities.BaseConfirmationToken;
import com.Abdelwahab.Live.With.ME.baseEntities.BaseUser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDate;
@Getter
@Setter
@NoArgsConstructor
@Entity
public class ClientConfirmationToken extends BaseConfirmationToken {

    @ManyToOne
    @JoinColumn(nullable = false )
    private Client client;

    public ClientConfirmationToken(String token, Client client){//}, Timestamp createdAt, Timestamp expiresAt, Client client) {
        super(token);//,createdAt,expiresAt);
        this.client = client;
    }

}
