package simon.subscriberservice.repository;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import simon.subscriberservice.model.ReleaseToggle;

import java.util.List;

@Repository
public class ReleaseToggleDatabase {
    private final JdbcClient jdbc;

    public ReleaseToggleDatabase(JdbcClient jdbc) {
        this.jdbc = jdbc;
    }

    public List<ReleaseToggle> findAll() {
        return jdbc.sql("SELECT name, enabled FROM release_toggle ORDER BY name").query(ReleaseToggle.class).list();
    }

    public boolean isEnabled(String name) {
        return jdbc.sql("SELECT enabled FROM release_toggle WHERE name = ?")
                .param(name)
                .query(Boolean.class)
                .optional()
                .orElse(false);
    }

    public boolean setEnabled(String name, boolean enabled) {
        return jdbc.sql("UPDATE release_toggle SET enabled = ? WHERE name = ?").params(enabled, name).update() > 0;
    }
}
