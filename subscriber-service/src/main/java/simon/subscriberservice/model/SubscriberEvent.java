package simon.subscriberservice.model;

// The message put on the subscriber queue every time someone subscribes or unsubscribes
public record SubscriberEvent(Type type, Long id, String email) {
    public enum Type { SUBSCRIBED, UNSUBSCRIBED }
}
