package pe.edu.vallegrande.vg_ms_competency.application.service;

import pe.edu.vallegrande.vg_ms_competency.domain.model.Competency;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CompetencyService {
    Flux<Competency> listAll();
    Mono<Competency> createCompetency(Competency competency);
    Mono<Competency> updateCompetency(String id, Competency competency);
    Mono<Competency> findById(String id);
    Flux<Competency> findByCourseId(String courseId);
}
