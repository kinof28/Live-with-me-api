package com.Abdelwahab.Live.With.ME.mappers;

import com.Abdelwahab.Live.With.ME.dto.ClientDTO;
import com.Abdelwahab.Live.With.ME.entities.Client;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component
@AllArgsConstructor
public class ClientMapper {
    private ArticleMapper articleMapper;
//    private DealMapper dealMapper;

    public ClientDTO getDtO(Client client) {
        if (client != null)
            return new ClientDTO(client.getId(),
                    client.getEmail(),
                    client.getFirstName(), client.getLastName(), client.getPhoneNumber(), client.getAddress(),
                    client.getProfileImg(), client.getIsSeller(),this.articleMapper.getDtos(client.getPublishedArticles()), this.articleMapper.getDtos(client.getInterestingArticles()));//,
//                    dealMapper.getDtos(client.getDeals()));
        else return null;
    }

    public List<ClientDTO> getsDtos(List<Client> clients) {
        List<ClientDTO> dtos = new LinkedList<>();
        for (Client c : clients) {
            dtos.add(this.getDtO(c));
        }
        return dtos;
    }
}
