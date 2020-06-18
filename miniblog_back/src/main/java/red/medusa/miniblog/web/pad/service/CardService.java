package red.medusa.miniblog.web.pad.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import red.medusa.miniblog.web.pad.bean.Card;
import red.medusa.miniblog.web.pad.mapper.CardMapper;

import java.util.Objects;

@Service
@Transactional
public class CardService {

    @Autowired
    private CardMapper cardMapper;
    
    public Card findById(String id) {
        return cardMapper.findById(id);
    }

    public void putCard(String id, Card Card) {
        Card c = cardMapper.findById(id);
        if(Objects.isNull(c)){
            cardMapper.insert(Card);
        }else{
            cardMapper.update(Card);
        }
    }
}
