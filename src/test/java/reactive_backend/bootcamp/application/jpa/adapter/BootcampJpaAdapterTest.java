package reactive_backend.bootcamp.application.jpa.adapter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactive_backend.bootcamp.application.jpa.entity.BootcampEntity;
import reactive_backend.bootcamp.application.jpa.mapper.IBootcampEntityMapper;
import reactive_backend.bootcamp.application.jpa.repository.IBootcampRepository;
import reactive_backend.bootcamp.domain.model.Bootcamp;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

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
}