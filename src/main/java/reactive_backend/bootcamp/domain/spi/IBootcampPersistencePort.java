package reactive_backend.bootcamp.domain.spi;

import reactive_backend.bootcamp.domain.model.Bootcamp;
import reactor.core.publisher.Mono;

public interface IBootcampPersistencePort {
    Mono<Bootcamp> createBootcamp(Bootcamp bootcamp);
}
