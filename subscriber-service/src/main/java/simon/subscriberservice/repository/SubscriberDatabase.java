package simon.subscriberservice.repository;

import io.micrometer.observation.annotation.Observed;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import simon.subscriberservice.model.Subscriber;

import java.util.List;
import java.util.Optional;

@Repository
@Observed(name = "subscriber.database")
public class SubscriberDatabase {
    private final JdbcClient jdbc;

    public SubscriberDatabase(JdbcClient jdbc) {
        this.jdbc = jdbc;
    }

    public List<Subscriber> findAll(int page, int pageSize) {
        return jdbc.sql("SELECT id, email FROM subscriber ORDER BY id LIMIT ? OFFSET ?")
                .params(pageSize, page * pageSize)
                .query(Subscriber.class)
                .list();
    }

    public Optional<Subscriber> findById(Long id) {
        return jdbc.sql("SELECT id, email FROM subscriber WHERE id = ?").param(id).query(Subscriber.class).optional();
    }

    public Subscriber insert(Subscriber subscriber) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.sql("INSERT INTO subscriber (email) VALUES (?)")
                .param(subscriber.getEmail())
                .update(keyHolder, "id");
        subscriber.setId(keyHolder.getKeyAs(Long.class));
        return subscriber;
    }

    public boolean delete(Long id) {
        return jdbc.sql("DELETE FROM subscriber WHERE id = ?").param(id).update() > 0;
    }
}
