package com.Abdelwahab.Live.With.ME.services;

import com.Abdelwahab.Live.With.ME.entities.HelpMessage;
import com.Abdelwahab.Live.With.ME.repositories.HelpMessageRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class HelpService {
    private HelpMessageRepository helpMessageRepository;


    public void submitHelpRequest(HelpMessage message){
        this.helpMessageRepository.save(message);
    }
}
