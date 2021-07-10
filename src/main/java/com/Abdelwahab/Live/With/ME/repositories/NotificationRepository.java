package com.Abdelwahab.Live.With.ME.repositories;

import com.Abdelwahab.Live.With.ME.entities.Client;
import com.Abdelwahab.Live.With.ME.entities.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification,Long> {
    List<Notification> findByClient(Client client);
}
