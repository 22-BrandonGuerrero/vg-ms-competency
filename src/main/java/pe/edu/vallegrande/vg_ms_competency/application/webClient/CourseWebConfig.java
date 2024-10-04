package pe.edu.vallegrande.vg_ms_competency.application.webClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class CourseWebConfig {

    private final WebClient.Builder webClientBuilder;
    private final String courseServiceUrl;

    @Autowired
    public CourseWebConfig(WebClient.Builder webClientBuilder, @Value("${spring.vg-ms-course.url}") String courseServiceUrl) {
        this.webClientBuilder = webClientBuilder;
        this.courseServiceUrl = courseServiceUrl;
    }

    public Mono<String> getCourseById(String id) {
        return webClientBuilder.build()
                .get()
                .uri(courseServiceUrl + "/{id}", id)
                .retrieve()
                .bodyToMono(String.class);
    }
}