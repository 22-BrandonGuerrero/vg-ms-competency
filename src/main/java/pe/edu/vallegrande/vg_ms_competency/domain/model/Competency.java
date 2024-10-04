package pe.edu.vallegrande.vg_ms_competency.domain.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "competency")
public class Competency {
    @Id
    private String idCompetency;
    private String idCourse;
    private String nameCompetency;
    private String description;

}
