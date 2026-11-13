package simon.publisherservice.model;

// The message put on the article queue
public record Article(String title, String content, String continent) {
}
