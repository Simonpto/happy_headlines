package simon.newsletterservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import simon.newsletterservice.model.Subscriber;
import simon.newsletterservice.service.NewsletterService;

import java.util.List;
import java.util.Map;

@RestController
public class NewsletterController {
    private final NewsletterService service;

    public NewsletterController(NewsletterService service) {
        this.service = service;
    }

    @PostMapping("/subscribers")
    @ResponseStatus(HttpStatus.CREATED)
    Subscriber subscribe(@RequestBody Subscriber subscriber) {
        return service.subscribe(subscriber);
    }

    @GetMapping("/subscribers")
    List<Subscriber> getSubscribers() {
        return service.findAll();
    }

    @PostMapping("/newsletters/{continent}/send")
    Map<String, Integer> sendNewsletter(@PathVariable String continent) {
        return Map.of("sentTo", service.sendNewsletter(continent));
    }
}
