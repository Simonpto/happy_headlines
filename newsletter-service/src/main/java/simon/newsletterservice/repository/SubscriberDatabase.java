package simon.newsletterservice.repository;

import io.micrometer.observation.annotation.Observed;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import simon.newsletterservice.model.Subscriber;

import java.util.List;

@Repository
@Observed(name = "subscriber.database")
public class SubscriberDatabase {
    private final JdbcClient jdbc;

    public SubscriberDatabase(JdbcClient jdbc) {
        this.jdbc = jdbc;
    }

    public List<Subscriber> findAll() {
        return jdbc.sql("SELECT id, email FROM subscriber ORDER BY id").query(Subscriber.class).list();
    }

    public Subscriber insert(Subscriber subscriber) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.sql("INSERT INTO subscriber (email) VALUES (?)")
                .param(subscriber.getEmail())
                .update(keyHolder, "id");
        subscriber.setId(keyHolder.getKeyAs(Long.class));
        return subscriber;
    }
}
