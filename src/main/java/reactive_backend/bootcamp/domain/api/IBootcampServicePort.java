package reactive_backend.bootcamp.domain.api;

import reactive_backend.bootcamp.domain.model.Bootcamp;
import reactive_backend.bootcamp.domain.model.PageCustom;
import reactor.core.publisher.Mono;

public interface IBootcampServicePort {
    Mono<Bootcamp> createBootcamp(Bootcamp bootcamp);
    Mono<PageCustom<Bootcamp>> getAllBootcamps(String orderDirection,String sortField,
                                               Integer pageSize, Integer currentPage);

}
