package simon.publisherservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import simon.publisherservice.model.Article;
import simon.publisherservice.service.PublisherService;

@RestController
@RequestMapping("/publish")
public class PublisherController {
    private final PublisherService service;

    public PublisherController(PublisherService service) {
        this.service = service;
    }

    // 202: the article is queued, the article-service stores it a moment later
    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    void publish(@RequestBody Article article) {
        service.publish(article);
    }
}
