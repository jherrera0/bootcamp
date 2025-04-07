package reactive_backend.bootcamp.application.http.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import reactive_backend.bootcamp.domain.model.Ability;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddBootcampDtoRequest {
    private Integer bootcampId;
    private List<Ability> abilitiesIds;
}
