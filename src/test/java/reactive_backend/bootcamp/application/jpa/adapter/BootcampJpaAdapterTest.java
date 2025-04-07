package reactive_backend.bootcamp.application.jpa.adapter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Pageable;
import reactive_backend.bootcamp.application.jpa.entity.BootcampEntity;
import reactive_backend.bootcamp.application.jpa.mapper.IBootcampEntityMapper;
import reactive_backend.bootcamp.application.jpa.repository.IBootcampRepository;
import reactive_backend.bootcamp.domain.model.Bootcamp;
import reactive_backend.bootcamp.domain.model.PageCustom;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class BootcampJpaAdapterTest {

    @Mock
    private IBootcampRepository bootcampRepository;

    @Mock
    private IBootcampEntityMapper bootcampEntityMapper;

    @InjectMocks
    private BootcampJpaAdapter bootcampJpaAdapter;

    AutoCloseable closeable;
    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @Test
    void createBootcampSuccessfully() {
        Bootcamp bootcamp = new Bootcamp();
        when(bootcampEntityMapper.toEntity(any())).thenReturn(new BootcampEntity());
        when(bootcampRepository.save(any())).thenReturn(Mono.just(new BootcampEntity()));
        when(bootcampEntityMapper.toDomain(any())).thenReturn(bootcamp);

        Mono<Bootcamp> result = bootcampJpaAdapter.createBootcamp(bootcamp);

        StepVerifier.create(result)
                .expectNext(bootcamp)
                .verifyComplete();
    }

    @Test
    void getAllBootcampsSuccessfully() {
        List<BootcampEntity> bootcampEntities = List.of(new BootcampEntity(), new BootcampEntity());
        when(bootcampRepository.findAllBy(any(Pageable.class))).thenReturn(Flux.fromIterable(bootcampEntities));
        when(bootcampRepository.count()).thenReturn(Mono.just(2L));
        when(bootcampEntityMapper.toDomainList(any())).thenReturn(List.of(new Bootcamp(), new Bootcamp()));

        Mono<PageCustom<Bootcamp>> result = bootcampJpaAdapter.getAllBootcamps(2, 0);

        StepVerifier.create(result)
                .assertNext(page -> {
                    assertEquals(0, page.getCurrentPage());
                    assertEquals(2, page.getPageSize());
                    assertEquals(1, page.getTotalPages());
                    assertEquals(2, page.getItems().size());
                })
                .verifyComplete();
    }
}