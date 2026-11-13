package simon.commentservice.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import simon.commentservice.client.ProfanityClient;
import simon.commentservice.model.Comment;
import simon.commentservice.repository.CommentRepository;

@Service
public class CommentService {
    private CommentRepository repository;
    private final ProfanityClient profanityClient;
    public CommentService(CommentRepository repository, ProfanityClient profanityClient) {
        this.profanityClient = profanityClient;
        this.repository = repository;
    }
    public Comment findById(long id){
        return this.repository.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
    public Comment create(Comment comment){
        comment.setContent(profanityClient.filter(comment.getContent()));
        return this.repository.save(comment);
    }
}
