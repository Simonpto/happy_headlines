package simon.subscriberservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import simon.subscriberservice.model.Subscriber;
import simon.subscriberservice.service.SubscriberService;

import java.util.List;

@RestController
@RequestMapping("/subscribers")
public class SubscriberController {
    private final SubscriberService service;

    public SubscriberController(SubscriberService service) {
        this.service = service;
    }

    @GetMapping
    List<Subscriber> getSubscribers(@RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "20") int pageSize) {
        return service.findAll(Math.max(page, 0), Math.clamp(pageSize, 1, 100));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    Subscriber subscribe(@RequestBody Subscriber subscriber) {
        return service.subscribe(subscriber);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void unsubscribe(@PathVariable Long id) {
        service.unsubscribe(id);
    }
}
