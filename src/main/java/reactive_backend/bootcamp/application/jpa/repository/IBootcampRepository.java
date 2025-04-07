package reactive_backend.bootcamp.application.jpa.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactive_backend.bootcamp.application.jpa.entity.BootcampEntity;

public interface IBootcampRepository extends ReactiveCrudRepository<BootcampEntity, Integer> {
}
