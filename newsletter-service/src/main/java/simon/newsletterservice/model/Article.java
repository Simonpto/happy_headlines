package simon.newsletterservice.model;

// Used both for articles from the queue and for articles fetched from the article-service
public record Article(Long id, String title, String content, String continent) {
}
