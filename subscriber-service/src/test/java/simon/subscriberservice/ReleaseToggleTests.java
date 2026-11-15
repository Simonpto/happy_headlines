package simon.subscriberservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.server.ResponseStatusException;
import simon.subscriberservice.service.ReleaseToggleService;
import simon.subscriberservice.service.SubscriberService;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class ReleaseToggleTests {

    @Autowired
    SubscriberService subscribers;

    @Autowired
    ReleaseToggleService toggles;

    @Test
    void subscriberServiceOnlyWorksWhileItsToggleIsOn() {
        toggles.set(ReleaseToggleService.SUBSCRIBER_SERVICE, false);
        assertThatThrownBy(() -> subscribers.findAll(0, 20)).isInstanceOf(ResponseStatusException.class);

        toggles.set(ReleaseToggleService.SUBSCRIBER_SERVICE, true);
        assertThatCode(() -> subscribers.findAll(0, 20)).doesNotThrowAnyException();
    }
}
