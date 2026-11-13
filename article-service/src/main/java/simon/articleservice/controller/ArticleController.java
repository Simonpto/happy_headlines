package simon.articleservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import simon.articleservice.model.Article;
import simon.articleservice.model.Continent;
import simon.articleservice.service.ArticleService;

import java.util.List;

@RestController
@RequestMapping("/articles/{continent}")
public class ArticleController {
    private final ArticleService service;

    public ArticleController(ArticleService service) {
        this.service = service;
    }

    @GetMapping
    List<Article> getArticles(@PathVariable String continent) {
        return service.findAll(toContinent(continent));
    }

    @GetMapping("/{id}")
    Article getArticle(@PathVariable String continent, @PathVariable Long id) {
        return service.findById(toContinent(continent), id).orElseThrow(() -> notFound(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    Article newArticle(@PathVariable String continent, @RequestBody Article newArticle) {
        return service.create(toContinent(continent), newArticle);
    }

    @PutMapping("/{id}")
    Article replaceArticle(@PathVariable String continent, @PathVariable Long id, @RequestBody Article replaceArticle) {
        return service.update(toContinent(continent), id, replaceArticle).orElseThrow(() -> notFound(id));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteArticle(@PathVariable String continent, @PathVariable Long id) {
        if (!service.delete(toContinent(continent), id)) {
            throw notFound(id);
        }
    }

    private static Continent toContinent(String value) {
        try {
            return Continent.fromPath(value);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unknown continent: " + value);
        }
    }

    private static ResponseStatusException notFound(Long id) {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, "Article " + id + " not found");
    }
}
