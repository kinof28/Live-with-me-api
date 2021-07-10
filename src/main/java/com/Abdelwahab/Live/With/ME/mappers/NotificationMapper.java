package com.Abdelwahab.Live.With.ME.mappers;

import com.Abdelwahab.Live.With.ME.dto.NotificationDTO;
import com.Abdelwahab.Live.With.ME.entities.Notification;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
@AllArgsConstructor
public class NotificationMapper {
    private ClientMapper clientMapper;
    private ArticleMapper articleMapper;
    public NotificationDTO getDTO(Notification notification){
        return new NotificationDTO(notification.getId(),
                                    notification.getType(),
//                                    clientMapper.getDtO(notification.getClient()),
                                    clientMapper.getDtO((notification.getOrigin())),
                                    articleMapper.getDto(notification.getArticle()));
    }
    public List<NotificationDTO> getDtos(List<Notification> notifications){
        List<NotificationDTO> notificationDTOS=new LinkedList<>();
        for(Notification notification:notifications){
            notificationDTOS.add(this.getDTO(notification));
        }
        return notificationDTOS;
    }
}
