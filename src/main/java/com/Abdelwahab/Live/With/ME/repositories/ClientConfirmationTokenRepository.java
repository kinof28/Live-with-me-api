package com.Abdelwahab.Live.With.ME.repositories;

import com.Abdelwahab.Live.With.ME.entities.ClientConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientConfirmationTokenRepository extends JpaRepository<ClientConfirmationToken , Long> {

    Optional<ClientConfirmationToken> findByToken(String s);

}
