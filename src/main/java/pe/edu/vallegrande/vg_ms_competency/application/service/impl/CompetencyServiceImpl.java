package pe.edu.vallegrande.vg_ms_competency.application.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.vallegrande.vg_ms_competency.application.service.CompetencyService;
import pe.edu.vallegrande.vg_ms_competency.domain.model.Competency;
import pe.edu.vallegrande.vg_ms_competency.domain.repository.CompetencyRepository;
import pe.edu.vallegrande.vg_ms_competency.application.webClient.CourseWebConfig;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;

@Service
public class CompetencyServiceImpl implements CompetencyService {

    private final CompetencyRepository competencyRepository;
    private final CourseWebConfig courseService;
    private final ObjectMapper objectMapper;

    @Autowired
    public CompetencyServiceImpl(CompetencyRepository competencyRepository, CourseWebConfig courseService, ObjectMapper objectMapper) {
        this.competencyRepository = competencyRepository;
        this.courseService = courseService;
        this.objectMapper = objectMapper;
    }

    @Override
    public Flux<Competency> listAll() {
        return competencyRepository.findAll()
                .flatMap(this::mapCourseNameToCompetency);
    }

    @Override
    public Mono<Competency> createCompetency(Competency competency) {
        return courseService.getCourseById(competency.getIdCourse())
                .flatMap(courseJson -> Mono.fromCallable(() -> extractCourseNameFromJson(courseJson))
                        .flatMap(nameCourse -> {
                            competency.setIdCourse(nameCourse); // Asignar nombre del curso
                            return competencyRepository.save(competency);
                        }))
                .onErrorResume(e -> {
                    // Si hay un error (por ejemplo, el microservicio 'course' no está disponible), guardar con el ID del curso
                    return competencyRepository.save(competency);
                });
    }

    @Override
    public Mono<Competency> updateCompetency(String id, Competency competency) {
        return competencyRepository.findById(id)
                .flatMap(existingCompetency -> {
                    // Actualiza los campos de la competencia
                    existingCompetency.setNameCompetency(competency.getNameCompetency());
                    existingCompetency.setDescription(competency.getDescription());
                    existingCompetency.setIdCourse(competency.getIdCourse()); // Actualiza el idCourse también

                    // Guarda los cambios en la base de datos
                    return competencyRepository.save(existingCompetency);
                });
    }


    @Override
    public Mono<Competency> findById(String id) {
        return competencyRepository.findById(id)
                .flatMap(this::mapCourseNameToCompetency);
    }

    @Override
    public Flux<Competency> findByCourseId(String courseId) {
        return competencyRepository.findByIdCourse(courseId)
                .flatMap(this::mapCourseNameToCompetency);
    }



    private Mono<Competency> mapCourseNameToCompetency(Competency competency) {
        return courseService.getCourseById(competency.getIdCourse())
                .map(courseJson -> {
                    String nameCourse = extractCourseNameFromJson(courseJson);
                    competency.setIdCourse(nameCourse); // Asignar nombre del curso
                    return competency;
                })
                .onErrorResume(e -> {
                    // Si hay un error (por ejemplo, el microservicio 'course' no está disponible), devolver la competencia con el ID del curso
                    return Mono.just(competency);
                });
    }

    private String extractCourseNameFromJson(String courseJson) {
        try {
            JsonNode jsonNode = objectMapper.readTree(courseJson);
            return jsonNode.get("nameCourse").asText();
        } catch (IOException e) {
            e.printStackTrace();
            return "Curso Desconocido";
        }
    }
}
