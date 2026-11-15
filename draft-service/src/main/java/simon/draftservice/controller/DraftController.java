package simon.draftservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import simon.draftservice.model.Draft;
import simon.draftservice.service.DraftService;

import java.util.List;

@RestController
@RequestMapping("/drafts")
public class DraftController {
    private final DraftService service;

    public DraftController(DraftService service) {
        this.service = service;
    }

    @GetMapping
    List<Draft> getDrafts(@RequestParam(defaultValue = "0") int page,
                          @RequestParam(defaultValue = "20") int pageSize) {
        return service.findAll(Math.max(page, 0), Math.clamp(pageSize, 1, 100));
    }

    @GetMapping("/{id}")
    Draft getDraft(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    Draft newDraft(@RequestBody Draft draft) {
        return service.create(draft);
    }

    @PutMapping("/{id}")
    Draft replaceDraft(@PathVariable Long id, @RequestBody Draft draft) {
        return service.update(id, draft);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteDraft(@PathVariable Long id) {
        service.delete(id);
    }

    @PostMapping("/{id}/publish")
    Draft publishDraft(@PathVariable Long id) {
        return service.publish(id);
    }
}
