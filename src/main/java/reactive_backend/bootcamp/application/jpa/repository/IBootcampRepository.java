package reactive_backend.bootcamp.application.jpa.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactive_backend.bootcamp.application.jpa.entity.BootcampEntity;
import reactor.core.publisher.Flux;

public interface IBootcampRepository extends ReactiveCrudRepository<BootcampEntity, Integer> {
    Flux<BootcampEntity> findAllBy(Pageable pageable);
}
