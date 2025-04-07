package reactive_backend.bootcamp.application.jpa.adapter;

import lombok.AllArgsConstructor;
import reactive_backend.bootcamp.application.jpa.mapper.IBootcampEntityMapper;
import reactive_backend.bootcamp.application.jpa.repository.IBootcampRepository;
import reactive_backend.bootcamp.domain.model.Bootcamp;
import reactive_backend.bootcamp.domain.spi.IBootcampPersistencePort;
import reactor.core.publisher.Mono;
@AllArgsConstructor
public class BootcampJpaAdapter implements IBootcampPersistencePort {
    private final IBootcampRepository bootcampRepository;
    private final IBootcampEntityMapper bootcampEntityMapper;
    @Override
    public Mono<Bootcamp> createBootcamp(Bootcamp bootcamp) {
        return bootcampRepository.save(bootcampEntityMapper.toEntity(bootcamp))
                .map(bootcampEntityMapper::toDomain);
    }
}
