package com.Abdelwahab.Live.With.ME.repositories;

import com.Abdelwahab.Live.With.ME.entities.ClientConfirmationToken;
import com.Abdelwahab.Live.With.ME.entities.ManagerConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ManagerConfirmationTokenRepository extends JpaRepository<ManagerConfirmationToken , Long> {


    Optional<ManagerConfirmationToken> findByToken(String s);
}
