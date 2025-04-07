package reactive_backend.bootcamp.domain.spi;

import reactive_backend.bootcamp.domain.model.Bootcamp;
import reactive_backend.bootcamp.domain.model.PageCustom;
import reactor.core.publisher.Mono;

public interface IBootcampPersistencePort {
    Mono<Bootcamp> createBootcamp(Bootcamp bootcamp);
    Mono<PageCustom<Bootcamp>> getAllBootcamps(Integer pageSize, Integer currentPage);
}
