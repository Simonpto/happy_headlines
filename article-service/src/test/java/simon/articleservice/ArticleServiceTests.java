package simon.articleservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import simon.articleservice.model.Article;
import simon.articleservice.model.Continent;
import simon.articleservice.service.ArticleService;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ArticleServiceTests {

    @Autowired
    ArticleService service;

    @Test
    void articleIsOnlyStoredInItsOwnContinentDatabase() {
        Article saved = service.create(Continent.EUROPE, new Article("Danish news", "Something happy"));

        assertThat(service.findById(Continent.EUROPE, saved.getId())).isPresent();
        assertThat(service.findAll(Continent.ASIA)).isEmpty();
        assertThat(service.findAll(Continent.GLOBAL)).isEmpty();
    }

    @Test
    void updateAndDeleteWorkWithinAContinent() {
        Article saved = service.create(Continent.AFRICA, new Article("Old", "Old content"));

        Article updated = service.update(Continent.AFRICA, saved.getId(), new Article("New", "New content")).orElseThrow();
        assertThat(updated.getTitle()).isEqualTo("New");

        assertThat(service.delete(Continent.AFRICA, saved.getId())).isTrue();
        assertThat(service.findById(Continent.AFRICA, saved.getId())).isEmpty();
    }
}
