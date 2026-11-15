package simon.subscriberservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import simon.subscriberservice.model.ReleaseToggle;
import simon.subscriberservice.repository.ReleaseToggleDatabase;

import java.util.List;

// The toggle is read from the database on every call, so flipping it takes effect immediately - no redeploy, no restart
@Service
public class ReleaseToggleService {
    public static final String SUBSCRIBER_SERVICE = "subscriber-service";
    private static final Logger log = LoggerFactory.getLogger(ReleaseToggleService.class);
    private final ReleaseToggleDatabase database;

    public ReleaseToggleService(ReleaseToggleDatabase database) {
        this.database = database;
    }

    public List<ReleaseToggle> findAll() {
        return database.findAll();
    }

    public ReleaseToggle set(String name, boolean enabled) {
        if (!database.setEnabled(name, enabled)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unknown release toggle: " + name);
        }
        log.info("Release toggle {} is now {}", name, enabled ? "ON" : "OFF");
        return new ReleaseToggle(name, enabled);
    }

    public void requireEnabled(String name) {
        if (!database.isEnabled(name)) {
            log.debug("Rejected call - release toggle {} is OFF", name);
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, name + " is not released yet");
        }
    }
}
