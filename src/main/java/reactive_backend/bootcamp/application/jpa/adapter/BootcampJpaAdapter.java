package reactive_backend.bootcamp.application.jpa.adapter;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import reactive_backend.bootcamp.application.jpa.mapper.IBootcampEntityMapper;
import reactive_backend.bootcamp.application.jpa.repository.IBootcampRepository;
import reactive_backend.bootcamp.domain.model.Bootcamp;
import reactive_backend.bootcamp.domain.model.PageCustom;
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

    @Override
    public Mono<PageCustom<Bootcamp>> getAllBootcamps(Integer pageSize, Integer currentPage) {
        Pageable pageable = PageRequest.of(currentPage, pageSize);
        return bootcampRepository.findAllBy(pageable)
                .collectList()
                .zipWith(bootcampRepository.count())
                .map(tuple -> new PageCustom<>(
                        currentPage,
                        pageSize,
                        (int) Math.ceil((double) tuple.getT2() / pageSize),
                        bootcampEntityMapper.toDomainList(tuple.getT1())
                ));
    }
}
