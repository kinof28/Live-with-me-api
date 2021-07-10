package com.Abdelwahab.Live.With.ME.entities;

import com.Abdelwahab.Live.With.ME.baseEntities.BaseNotification;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
public class Notification extends BaseNotification {
    @ManyToOne
    private Client origin;
    @ManyToOne
    private Article article;
}
