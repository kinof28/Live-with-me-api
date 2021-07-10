package com.Abdelwahab.Live.With.ME.baseEntities;

import com.Abdelwahab.Live.With.ME.entities.Client;
import com.Abdelwahab.Live.With.ME.enums.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@MappedSuperclass
public class BaseNotification {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false,updatable = false)
    private NotificationType type;
    @ManyToOne
    private Client client;
}
