package red.medusa.miniblog.web.pad.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import red.medusa.miniblog.web.pad.bean.Card;
import red.medusa.miniblog.web.pad.bean.Pad;
import red.medusa.miniblog.web.pad.service.CardService;
import red.medusa.miniblog.web.pad.service.PadService;

@RestController
@RequestMapping("/miniblog/data")
public class PadDataController {

    @Autowired
    private PadService padService;

    @Autowired
    private CardService cardService;

    @GetMapping(value = "/pad/{id}")
    public Pad getPad(@PathVariable String id) {
        return padService.findById(id);
    }

    @PutMapping(value = "/pad/{id}")
    public Pad putPad(@PathVariable String id, @RequestBody Pad pad) {
        padService.putPad(id, pad);
        return pad;
    }

    @DeleteMapping(value = "/pad/{id}")
    public String deletePad(@PathVariable String id) {
        Integer num = padService.deleteById(id);
        return "SUCCESS";
    }


    @GetMapping(value = "/card/{id}")
    public Card getCard(@PathVariable String id) {
        return cardService.findById(id);
    }

    @PutMapping(value = "/card/{id}")
    public Card putCard(@PathVariable String id, @RequestBody Card card) {
        cardService.putCard(id, card);
        return card;
    }


}
