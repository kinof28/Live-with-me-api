package com.Abdelwahab.Live.With.ME.baseEntities;

import com.Abdelwahab.Live.With.ME.enums.Role;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

@Setter
@Getter
@MappedSuperclass
public abstract class BaseUser implements UserDetails {
    //TODO: userDetails method implementation
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false,unique = true,updatable = false)
    private String email;
    @Column(nullable = false,name = "first_name")
    private String firstName;
    @Column(nullable = false,name="last_name")
    private String lastName;
    @Column(nullable = false)
    private String password;
    @Column(name="phone_number")
    private String phoneNumber;
    @Column(name="profile_img")
    private String profileImg;
    @Column(nullable = false,updatable = false)
    @Enumerated(value = EnumType.STRING)
    private Role role;
    @Column(nullable = false)
    private Boolean isActivated;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(this.role.name());
        return Collections.singletonList(simpleGrantedAuthority);
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.isActivated;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseUser baseUser = (BaseUser) o;
        return id.equals(baseUser.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, role);
    }
}
