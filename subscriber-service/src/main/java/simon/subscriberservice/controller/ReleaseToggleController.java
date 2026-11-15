package simon.subscriberservice.controller;

import org.springframework.web.bind.annotation.*;
import simon.subscriberservice.model.ReleaseToggle;
import simon.subscriberservice.service.ReleaseToggleService;

import java.util.List;

// Flip a toggle at runtime: PUT /toggles/subscriber-service {"enabled": true}
@RestController
@RequestMapping("/toggles")
public class ReleaseToggleController {
    private final ReleaseToggleService service;

    public ReleaseToggleController(ReleaseToggleService service) {
        this.service = service;
    }

    @GetMapping
    List<ReleaseToggle> getToggles() {
        return service.findAll();
    }

    @PutMapping("/{name}")
    ReleaseToggle setToggle(@PathVariable String name, @RequestBody ReleaseToggle toggle) {
        return service.set(name, toggle.enabled());
    }
}
