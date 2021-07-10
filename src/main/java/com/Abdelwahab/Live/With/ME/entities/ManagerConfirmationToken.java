package com.Abdelwahab.Live.With.ME.entities;

import com.Abdelwahab.Live.With.ME.baseEntities.BaseConfirmationToken;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@Entity
public class ManagerConfirmationToken extends BaseConfirmationToken {

    @ManyToOne
    @JoinColumn(nullable = false )
    private Manager manager;


}
