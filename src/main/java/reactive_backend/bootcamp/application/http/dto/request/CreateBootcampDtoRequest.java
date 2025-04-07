package reactive_backend.bootcamp.application.http.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateBootcampDtoRequest {
    private String name;
    private String description;
    private List<Integer> abilitiesIds;
}
