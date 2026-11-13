package simon.draftservice.service;

import io.micrometer.observation.annotation.Observed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.server.ResponseStatusException;
import simon.draftservice.model.Draft;
import simon.draftservice.repository.DraftDatabase;

import java.util.List;
import java.util.Map;

@Service
@Observed(name = "draft.service")
public class DraftService {
    private static final Logger log = LoggerFactory.getLogger(DraftService.class);
    private final DraftDatabase database;
    private final RestClient publisherRestClient;

    public DraftService(DraftDatabase database, RestClient publisherRestClient) {
        this.database = database;
        this.publisherRestClient = publisherRestClient;
    }

    public List<Draft> findAll() {
        return database.findAll();
    }

    public Draft findById(Long id) {
        return database.findById(id).orElseThrow(() -> notFound(id));
    }

    public Draft create(Draft draft) {
        draft.setPublished(false);
        Draft saved = database.insert(draft);
        log.info("Created draft {} for continent {}", saved.getId(), saved.getContinent());
        return saved;
    }

    public Draft update(Long id, Draft draft) {
        Draft existing = findById(id);
        draft.setId(id);
        draft.setPublished(existing.isPublished());
        database.update(draft);
        log.info("Updated draft {}", id);
        return draft;
    }

    public void delete(Long id) {
        if (!database.delete(id)) {
            throw notFound(id);
        }
        log.info("Deleted draft {}", id);
    }

    public Draft publish(Long id) {
        Draft draft = findById(id);
        if (draft.isPublished()) {
            log.warn("Draft {} is already published", id);
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Draft " + id + " is already published");
        }
        log.debug("Publishing draft {} to continent {}", id, draft.getContinent());
        try {
            publisherRestClient.post()
                    .uri("/publish")
                    .body(Map.of("title", draft.getTitle(), "content", draft.getContent(), "continent", draft.getContinent()))
                    .retrieve()
                    .toBodilessEntity();
        } catch (RestClientException e) {
            log.error("Could not publish draft {} - publisher-service failed", id, e);
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Publisher service is unavailable, try again later");
        }
        draft.setPublished(true);
        database.update(draft);
        log.info("Sent draft {} to the publisher-service", id);
        return draft;
    }

    private static ResponseStatusException notFound(Long id) {
        log.warn("Draft {} not found", id);
        return new ResponseStatusException(HttpStatus.NOT_FOUND, "Draft " + id + " not found");
    }
}
