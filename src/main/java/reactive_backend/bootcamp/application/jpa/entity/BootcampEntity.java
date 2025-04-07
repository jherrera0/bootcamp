package reactive_backend.bootcamp.application.jpa.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table("bootcamp_entity")
public class BootcampEntity {
    @Id
    private Integer id;

    @Column("name")
    private String name;

    @Column("description")
    private String description;
}
