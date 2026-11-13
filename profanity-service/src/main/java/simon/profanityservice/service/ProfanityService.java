package simon.profanityservice.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import simon.profanityservice.model.Profanity;
import simon.profanityservice.repository.ProfanityRepository;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProfanityService {
    private ProfanityRepository repository;
    public ProfanityService(ProfanityRepository repository) {
        this.repository = repository;
    }

    public Profanity findById(long id){
        return this.repository.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public Profanity create(Profanity profanity){
        return this.repository.save(profanity);
    }

    public String filter (String content){
        Set<String> bannedWords = repository.findAll().stream().map(w->w.getWord()
                .toLowerCase())
                .collect(Collectors.toSet());
        return Arrays.stream(content.split("\\s+")).map(word -> bannedWords
                .contains(word.toLowerCase()
                .replaceAll("\\p{Punct}", "")) ? "*"
                .repeat(word.length()) : word)
                .collect(Collectors.joining(" "));
    }

}
