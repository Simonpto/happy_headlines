package simon.articleservice.model;

// An article as it arrives on the article queue from the publisher-service
public record PublishedArticle(String title, String content, String continent) {
}
