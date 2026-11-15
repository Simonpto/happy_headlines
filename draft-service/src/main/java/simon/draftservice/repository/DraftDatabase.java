package simon.draftservice.repository;

import io.micrometer.observation.annotation.Observed;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import simon.draftservice.model.Draft;

import java.util.List;
import java.util.Optional;

@Repository
@Observed(name = "draft.database")
public class DraftDatabase {
    private final JdbcClient jdbc;

    public DraftDatabase(JdbcClient jdbc) {
        this.jdbc = jdbc;
    }

    public List<Draft> findAll(int page, int pageSize) {
        return jdbc.sql("SELECT id, title, content, continent, published FROM draft ORDER BY id DESC LIMIT ? OFFSET ?")
                .params(pageSize, page * pageSize)
                .query(Draft.class)
                .list();
    }

    public Optional<Draft> findById(Long id) {
        return jdbc.sql("SELECT id, title, content, continent, published FROM draft WHERE id = ?")
                .param(id)
                .query(Draft.class)
                .optional();
    }

    public Draft insert(Draft draft) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.sql("INSERT INTO draft (title, content, continent, published) VALUES (?, ?, ?, ?)")
                .params(draft.getTitle(), draft.getContent(), draft.getContinent(), draft.isPublished())
                .update(keyHolder, "id");
        draft.setId(keyHolder.getKeyAs(Long.class));
        return draft;
    }

    public boolean update(Draft draft) {
        int rows = jdbc.sql("UPDATE draft SET title = ?, content = ?, continent = ?, published = ? WHERE id = ?")
                .params(draft.getTitle(), draft.getContent(), draft.getContinent(), draft.isPublished(), draft.getId())
                .update();
        return rows > 0;
    }

    public boolean delete(Long id) {
        return jdbc.sql("DELETE FROM draft WHERE id = ?").param(id).update() > 0;
    }
}
