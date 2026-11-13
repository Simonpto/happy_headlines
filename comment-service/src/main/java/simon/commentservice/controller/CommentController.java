package simon.commentservice.controller;

import org.springframework.web.bind.annotation.*;
import simon.commentservice.model.Comment;
import simon.commentservice.service.CommentService;

@RestController
@RequestMapping("/comments")
public class CommentController {
    private CommentService service;
    public CommentController(CommentService service) {
        this.service = service;
    }
    @GetMapping("/{id}")
    public Comment getComment(@PathVariable long id) {
        return this.service.findById(id);
    }
    @PostMapping
    public Comment createComment(@RequestBody Comment comment) {
        return this.service.create(comment);
    }
}
