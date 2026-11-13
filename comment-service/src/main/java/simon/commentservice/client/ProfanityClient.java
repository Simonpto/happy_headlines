package simon.commentservice.client;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.server.ResponseStatusException;

@Component
public class ProfanityClient {
    private final RestClient profanityRestClient;
    public ProfanityClient(RestClient profanityRestClient) {
        this.profanityRestClient = profanityRestClient;
    }

    @CircuitBreaker(name = "profanity", fallbackMethod = "filterFallback")
    public String filter(String content){
        return profanityRestClient.post().uri("/profanities/filter").body(content).retrieve().body(String.class);
    }

    // Called when profanity-service fails or the circuit is open: refuse rather than save an unfiltered comment
    private String filterFallback(String content, Throwable error){
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Profanity service is unavailable, try again later");
    }
}
