package reactive_backend.bootcamp.application.http.handler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactive_backend.bootcamp.application.http.dto.request.CreateBootcampDtoRequest;
import reactive_backend.bootcamp.application.http.dto.request.ListBootcampsDtoRequest;
import reactive_backend.bootcamp.application.http.dto.response.BootcampDtoResponse;
import reactive_backend.bootcamp.application.http.dto.response.PageResponse;
import reactive_backend.bootcamp.application.http.mapper.ICreateBootcampDtoMapper;
import reactive_backend.bootcamp.application.http.mapper.IPageResponseMapper;
import reactive_backend.bootcamp.domain.api.IBootcampServicePort;
import reactive_backend.bootcamp.domain.model.Bootcamp;
import reactive_backend.bootcamp.domain.model.PageCustom;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

class BootcampHandlerTest {

    @Mock
    private IBootcampServicePort bootcampServicePort;

    @Mock
    private ICreateBootcampDtoMapper createBootcampDtoMapper;

    @Mock
    private IPageResponseMapper pageResponseMapper;

    @Mock
    private ServerRequest serverRequest;

    @InjectMocks
    private BootcampHandler bootcampHandler;

    AutoCloseable closeable;
    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @Test
    void createBootcampSuccessfully() {
        CreateBootcampDtoRequest dtoRequest = new CreateBootcampDtoRequest();
        when(serverRequest.bodyToMono(CreateBootcampDtoRequest.class)).thenReturn(Mono.just(dtoRequest));
        when(createBootcampDtoMapper.toDomain(any())).thenReturn(new Bootcamp(1,"","", List.of()));
        when(bootcampServicePort.createBootcamp(any()))
                .thenReturn(Mono.just(new Bootcamp(1,"","", List.of())));
        when(createBootcampDtoMapper.toDtoResponse(any()))
                .thenReturn(new BootcampDtoResponse(1,"","",List.of()));

        Mono<ServerResponse> response = bootcampHandler.createBootcamp(serverRequest);

        StepVerifier.create(response)
                .expectNextMatches(serverResponse -> serverResponse.statusCode().is2xxSuccessful())
                .verifyComplete();
    }

    @Test
    void createBootcampWithEmptyRequestBody() {
        when(serverRequest.bodyToMono(CreateBootcampDtoRequest.class)).thenReturn(Mono.empty());

        Mono<ServerResponse> response = bootcampHandler.createBootcamp(serverRequest);

        StepVerifier.create(response)
                .expectNextMatches(serverResponse -> serverResponse.statusCode().is4xxClientError())
                .verifyComplete();
    }

    @Test
    void createBootcampWithError() {
        CreateBootcampDtoRequest dtoRequest = new CreateBootcampDtoRequest();
        when(serverRequest.bodyToMono(CreateBootcampDtoRequest.class)).thenReturn(Mono.just(dtoRequest));
        when(createBootcampDtoMapper.toDomain(any())).thenReturn(new Bootcamp(1,"","", List.of()));
        when(bootcampServicePort.createBootcamp(any())).thenReturn(Mono.error(new RuntimeException("Service error")));

        Mono<ServerResponse> response = bootcampHandler.createBootcamp(serverRequest);

        StepVerifier.create(response)
                .expectNextMatches(serverResponse -> serverResponse.statusCode().is4xxClientError())
                .verifyComplete();
    }

    @Test
    void getAllBootcampsSuccessfully() {
        ListBootcampsDtoRequest dtoRequest =
                new ListBootcampsDtoRequest("name","asc",0,1);
        PageCustom<Bootcamp> pageCustom = new PageCustom<>();
        PageResponse<BootcampDtoResponse> pageResponse = new PageResponse<>();

        when(serverRequest.bodyToMono(ListBootcampsDtoRequest.class)).thenReturn(Mono.just(dtoRequest));
        when(bootcampServicePort.getAllBootcamps(any(), any(), anyInt(), anyInt()))
                .thenReturn(Mono.just(pageCustom));
        when(pageResponseMapper.toPageResponse(any())).thenReturn(pageResponse);

        Mono<ServerResponse> response = bootcampHandler.getAllBootcamps(serverRequest);

        StepVerifier.create(response)
                .expectNextMatches(serverResponse -> serverResponse.statusCode().is2xxSuccessful())
                .verifyComplete();

    }

    @Test
    void getAllBootcampsWithEmptyRequestBody() {
        when(serverRequest.bodyToMono(ListBootcampsDtoRequest.class)).thenReturn(Mono.empty());

        Mono<ServerResponse> response = bootcampHandler.getAllBootcamps(serverRequest);

        StepVerifier.create(response)
                .expectNextMatches(serverResponse -> serverResponse.statusCode().is4xxClientError())
                .verifyComplete();
    }

    @Test
    void getAllBootcampsWithError() {
        ListBootcampsDtoRequest dtoRequest = new ListBootcampsDtoRequest();
        when(serverRequest.bodyToMono(ListBootcampsDtoRequest.class)).thenReturn(Mono.just(dtoRequest));
        when(bootcampServicePort.getAllBootcamps(any(), any(), anyInt(), anyInt()))
                .thenReturn(Mono.error(new RuntimeException("Service error")));

        Mono<ServerResponse> response = bootcampHandler.getAllBootcamps(serverRequest);

        StepVerifier.create(response)
                .expectNextMatches(serverResponse -> serverResponse.statusCode().is4xxClientError())
                .verifyComplete();
    }
}