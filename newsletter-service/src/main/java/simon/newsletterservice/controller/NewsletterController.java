package simon.newsletterservice.controller;

import org.springframework.web.bind.annotation.*;
import simon.newsletterservice.service.NewsletterService;

import java.util.Map;

@RestController
public class NewsletterController {
    private final NewsletterService service;

    public NewsletterController(NewsletterService service) {
        this.service = service;
    }

    @PostMapping("/newsletters/{continent}/send")
    Map<String, Integer> sendNewsletter(@PathVariable String continent) {
        return Map.of("sentTo", service.sendNewsletter(continent));
    }
}
