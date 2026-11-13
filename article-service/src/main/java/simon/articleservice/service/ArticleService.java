package simon.articleservice.service;

import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;
import simon.articleservice.config.ArticleDatabase;
import simon.articleservice.model.Article;
import simon.articleservice.model.Continent;

import java.util.List;
import java.util.Optional;

@Service
public class ArticleService {
    private final ArticleDatabase database;

    public ArticleService(ArticleDatabase database) {
        this.database = database;
    }

    public List<Article> findAll(Continent continent) {
        return database.get(continent)
                .sql("SELECT id, title, content FROM article ORDER BY id")
                .query(Article.class)
                .list();
    }

    public Optional<Article> findById(Continent continent, Long id) {
        return database.get(continent)
                .sql("SELECT id, title, content FROM article WHERE id = ?")
                .param(id)
                .query(Article.class)
                .optional();
    }

    public Article create(Continent continent, Article article) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        database.get(continent)
                .sql("INSERT INTO article (title, content) VALUES (?, ?)")
                .params(article.getTitle(), article.getContent())
                .update(keyHolder, "id");
        article.setId(keyHolder.getKeyAs(Long.class));
        return article;
    }

    public Optional<Article> update(Continent continent, Long id, Article updated) {
        int rows = database.get(continent)
                .sql("UPDATE article SET title = ?, content = ? WHERE id = ?")
                .params(updated.getTitle(), updated.getContent(), id)
                .update();
        if (rows == 0) {
            return Optional.empty();
        }
        updated.setId(id);
        return Optional.of(updated);
    }

    public boolean delete(Continent continent, Long id) {
        int rows = database.get(continent)
                .sql("DELETE FROM article WHERE id = ?")
                .param(id)
                .update();
        return rows > 0;
    }
}
