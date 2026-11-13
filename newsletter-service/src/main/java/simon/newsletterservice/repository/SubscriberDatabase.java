package simon.newsletterservice.repository;

import io.micrometer.observation.annotation.Observed;
import org.springframework.jdbc.core.simple.JdbcClient;
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

    // Delete first so the same message can be received twice without breaking anything
    public void save(Subscriber subscriber) {
        delete(subscriber.getId());
        jdbc.sql("INSERT INTO subscriber (id, email) VALUES (?, ?)")
                .params(subscriber.getId(), subscriber.getEmail())
                .update();
    }

    public void delete(Long id) {
        jdbc.sql("DELETE FROM subscriber WHERE id = ?").param(id).update();
    }
}
