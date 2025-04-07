package reactive_backend.bootcamp.application.http.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactive_backend.bootcamp.application.http.dto.request.CreateBootcampDtoRequest;
import reactive_backend.bootcamp.application.http.dto.request.ListBootcampsDtoRequest;
import reactive_backend.bootcamp.application.http.mapper.ICreateBootcampDtoMapper;
import reactive_backend.bootcamp.application.http.mapper.IPageResponseMapper;
import reactive_backend.bootcamp.domain.api.IBootcampServicePort;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class BootcampHandler implements IBootcampHandler{
    private final IBootcampServicePort bootcampServicePort;
    private final ICreateBootcampDtoMapper createBootcampDtoMapper;
    private final IPageResponseMapper pageResponseMapper;

    @Override
    public Mono<ServerResponse> createBootcamp(ServerRequest request) {
        return request.bodyToMono(CreateBootcampDtoRequest.class).
                doOnNext(dto -> log.info("Datos recibidos desde Postman para crear bootcamp: {}", dto))
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Request body cannot be empty")))
                .map(createBootcampDtoMapper::toDomain)
                .doOnNext(dto -> log.info("Datos a devolver en respuesta de bootcamp creado: {}", dto))
                .flatMap(bootcampServicePort::createBootcamp)
                .map(createBootcampDtoMapper::toDtoResponse)
                .flatMap(bootcamp -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(bootcamp)
                )
                .onErrorResume(error -> {
                    log.error("Error al procesar la solicitud de crear bootcamp: {}", error.getMessage());
                    return ServerResponse.badRequest()
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(Map.of(
                                    "error", error.getMessage(),
                                    "timestamp", Instant.now()
                            ));
                });
    }

    @Override
    public Mono<ServerResponse> getAllBootcamps(ServerRequest request) {
        return request.bodyToMono(ListBootcampsDtoRequest.class)
                .doOnNext(dto -> log.info("Datos recibidos desde Postman para paginar: {}", dto))
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Request body list cannot be empty")))
                .flatMap(dto -> bootcampServicePort.getAllBootcamps(
                        dto.getSortOrder(),
                        dto.getSortField(),
                        dto.getPageSize(),
                        dto.getCurrentPage()))
                .map(pageResponseMapper::toPageResponse)
                .doOnNext(dto -> log.info("Datos a devolver en respuesta de bootcamp: {}", dto))
                .flatMap(list -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(list)
                )
                .onErrorResume(error -> {
                    log.error("Error al procesar la solicitud de paginacion: {}", error.getMessage());
                    return ServerResponse.badRequest()
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(Map.of(
                                    "error", error.getMessage(),
                                    "timestamp", Instant.now()
                            ));
                });
    }
}
