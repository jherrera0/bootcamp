package reactive_backend.bootcamp.domain.api;

import reactive_backend.bootcamp.domain.model.Bootcamp;
import reactor.core.publisher.Mono;

public interface IBootcampServicePort {
    Mono<Bootcamp> createBootcamp(Bootcamp bootcamp);
}
