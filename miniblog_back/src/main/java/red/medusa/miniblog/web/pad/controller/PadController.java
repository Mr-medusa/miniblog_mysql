package red.medusa.miniblog.web.pad.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import red.medusa.miniblog.web.pad.bean.Pad;
import red.medusa.miniblog.web.pad.service.PadService;
import red.medusa.miniblog.web.pad.vo.PadBox;
import red.medusa.miniblog.web.pad.vo.PadRoot;

import java.util.List;

@RestController
public class PadController {

    @Autowired
    private PadService padService;

    @GetMapping("/padRoot")
    public PadRoot getPadFolder() {
        return padService.getFolderFromPads();
    }

    @PostMapping(value = "/deleteCardAndPadByIds")
    public ResponseEntity deleteCardAndPadByIds(@RequestBody String[] ids) {
        return padService.deleteCardAndPadByIds(ids);
    }

    @PostMapping(value = "/moveToOther")
    public ResponseEntity moveToOther(@RequestBody List<Pad> pads) {
        return padService.moveToOther(pads);
    }

    @PostMapping(value = "/updatePad")
    public ResponseEntity updatePad(@RequestBody List<Pad> pads) {
        return padService.updatePad(pads);
    }

    @PostMapping(value = "/addPadToSepciPlace")
    public ResponseEntity addPadToSepciPlace(@RequestBody PadBox padBox) {
        return padService.addPadToSepciPlace(padBox);
    }

    @PostMapping(value = "/clear")
    public String clear() {
        padService.clear();
        return "clear";
    }

    @GetMapping(value = "/clear")
    public String clearForDev() {
        padService.clear();
        return "clear";
    }
}
