package simon.profanityservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import simon.profanityservice.model.Profanity;

public interface ProfanityRepository extends JpaRepository<Profanity, Long> {

}
