package com.Abdelwahab.Live.With.ME.entities;

import com.Abdelwahab.Live.With.ME.baseEntities.BaseUser;
import com.Abdelwahab.Live.With.ME.enums.Role;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Entity;
import java.util.Collection;

@Entity
public class Admin extends BaseUser {

}
