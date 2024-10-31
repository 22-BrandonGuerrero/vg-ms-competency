package pe.edu.vallegrande.vg_ms_competency.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pe.edu.vallegrande.vg_ms_competency.domain.model.Competency;
import pe.edu.vallegrande.vg_ms_competency.application.service.impl.CompetencyServiceImpl;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/directives/competency/api/v1")
public class CompetencyController {

    private final CompetencyServiceImpl competencyService;

    @Autowired
    public CompetencyController(CompetencyServiceImpl competencyService) {
        this.competencyService = competencyService;
    }

    @GetMapping("/all")
    public Flux<Competency> getListAll(){
        return competencyService.listAll();
    }

    @PostMapping("/create")
    public Mono<Competency> createCompetency(@RequestBody Competency competency) {
        return competencyService.createCompetency(competency);
    }

    @GetMapping("/{id}")
    public Mono<Competency> getCompetencyById(@PathVariable String id) {
        return competencyService.findById(id);
    }

    @PutMapping("/{id}")
    public Mono<Competency> updateCompetency(@PathVariable String id, @RequestBody Competency competency) {
        return competencyService.updateCompetency(id, competency);
    }

    @GetMapping("/course/{courseId}")
    public Flux<Competency> competencyForCourse(@PathVariable String courseId) {
        return competencyService.findByCourseId(courseId);
    }



}