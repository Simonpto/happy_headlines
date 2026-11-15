package simon.newsletterservice.model;

// A message from the subscriber-service on the subscriber queue
public record SubscriberEvent(Type type, Long id, String email) {
    public enum Type { SUBSCRIBED, UNSUBSCRIBED }
}
