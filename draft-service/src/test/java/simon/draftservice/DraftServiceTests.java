package simon.draftservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.server.ResponseStatusException;
import simon.draftservice.model.Draft;
import simon.draftservice.service.DraftService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class DraftServiceTests {

    @Autowired
    DraftService service;

    @Test
    void draftsCanBeCreatedUpdatedAndDeleted() {
        Draft saved = service.create(new Draft("First try", "Something happy", "europe"));
        assertThat(service.findById(saved.getId()).getTitle()).isEqualTo("First try");

        service.update(saved.getId(), new Draft("Second try", "Something happier", "europe"));
        assertThat(service.findById(saved.getId()).getTitle()).isEqualTo("Second try");

        service.delete(saved.getId());
        assertThatThrownBy(() -> service.findById(saved.getId())).isInstanceOf(ResponseStatusException.class);
    }
}
