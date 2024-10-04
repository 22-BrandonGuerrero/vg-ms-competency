package pe.edu.vallegrande.vg_ms_competency.domain.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import pe.edu.vallegrande.vg_ms_competency.domain.model.Competency;
import reactor.core.publisher.Flux;

public interface CompetencyRepository extends ReactiveMongoRepository<Competency, String> {
    Flux<Competency> findByIdCourse(String courseId);

}
