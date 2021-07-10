package com.Abdelwahab.Live.With.ME.mappers;

import com.Abdelwahab.Live.With.ME.dto.DealDTO;
import com.Abdelwahab.Live.With.ME.entities.Client;
import com.Abdelwahab.Live.With.ME.entities.Deal;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component
@AllArgsConstructor
public class DealMapper {
    private ArticleMapper articleMapper;
    private ClientMapper clientMapper;
    public DealDTO getDto(Deal deal){
        if(deal!=null){

            return new DealDTO(deal.getId(),deal.getOpen(),articleMapper.getDto(deal.getArticle()),
                                this.clientMapper.getsDtos(new LinkedList<>(deal.getRequesters())),
                                this.clientMapper.getsDtos(new LinkedList<>(deal.getBuyers())));
        }
        return null;
    }
    public List<DealDTO> getDtos(List<Deal> deals){
        List<DealDTO> dealDTOS=new LinkedList<>();
        for(Deal d:deals){
            dealDTOS.add(this.getDto(d));
        }
        return dealDTOS;
    }

}
