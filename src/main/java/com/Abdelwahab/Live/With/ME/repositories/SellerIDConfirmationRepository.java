package com.Abdelwahab.Live.With.ME.repositories;

import com.Abdelwahab.Live.With.ME.entities.Client;
import com.Abdelwahab.Live.With.ME.entities.SellerIDConfirmationRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SellerIDConfirmationRepository extends JpaRepository<SellerIDConfirmationRequest,Long> {


    Optional<SellerIDConfirmationRequest> findByClient(Client client);
}
