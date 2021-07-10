package com.Abdelwahab.Live.With.ME.dto;

import com.Abdelwahab.Live.With.ME.enums.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class NotificationDTO {
    private Long id;
    private NotificationType type;
//    private ClientDTO client;
    private ClientDTO origin;
    private ArticleDTO article;
}
