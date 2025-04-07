package reactive_backend.bootcamp.application.http.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListBootcampsDtoRequest {
    private String sortField;
    private String sortOrder;
    private Integer currentPage;
    private Integer pageSize;
}
