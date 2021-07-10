package com.Abdelwahab.Live.With.ME.baseEntities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Date;

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
public class BaseConfirmationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false , updatable = false , unique = true )
    private String token;
//    @Column(nullable = false , updatable = false)
//    @Temporal(value = TemporalType.TIMESTAMP)
//    private Date createdAt;
//    @Column(nullable = false , updatable = false)
//    @Temporal(value = TemporalType.TIMESTAMP)
//    private Date expiresAt;
//    @Temporal(value = TemporalType.TIMESTAMP)
//    private Date confirmedAt;

    public BaseConfirmationToken(String token){//, Timestamp createdAt, Timestamp expiresAt) {
        this.token = token;
//        this.createdAt = createdAt;
//        this.expiresAt = expiresAt;
    }
}
