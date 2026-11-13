package simon.profanityservice.controller;

import org.springframework.web.bind.annotation.*;
import simon.profanityservice.model.Profanity;
import simon.profanityservice.service.ProfanityService;

@RestController
@RequestMapping("/profanities")
public class ProfanityController {
    private ProfanityService service;
    public ProfanityController(ProfanityService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public Profanity getProfanity(@PathVariable long id) {
        return this.service.findById(id);
    }

    @PostMapping
    public Profanity createProfanity(@RequestBody Profanity profanity) {
        return this.service.create(profanity);
    }

    @PostMapping("/filter")
    public String filter(@RequestBody String content){
        return this.service.filter(content);
    }
}
