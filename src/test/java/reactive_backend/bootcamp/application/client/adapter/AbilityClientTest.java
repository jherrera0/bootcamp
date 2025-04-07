package reactive_backend.bootcamp.application.client.adapter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import reactive_backend.bootcamp.application.http.dto.request.AddBootcampDtoRequest;
import reactive_backend.bootcamp.application.http.dto.request.GetAbilitiesByIdsDtoRequest;
import reactive_backend.bootcamp.domain.model.Ability;
import reactive_backend.bootcamp.domain.util.ConstRoute;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AbilityClientTest {

    private AbilityClient abilityClient;

    @Mock
    private WebClient.Builder webClientBuilder;

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestBodyUriSpec requestBodyUriSpec;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.RequestBodySpec requestBodySpec;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    AutoCloseable closeable;
    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);

        when(webClientBuilder.baseUrl(ConstRoute.ABILITY_REST_ROUTE)).thenReturn(webClientBuilder);
        when(webClientBuilder.build()).thenReturn(webClient);

        abilityClient = new AbilityClient(webClientBuilder);
    }

    @Test
    void findAllAbilitiesByIds_ShouldReturnListOfAbilities() {
        // Arrange
        List<Integer> abilityIds = Arrays.asList(1, 2, 3);
        List<Ability> expectedAbilities = Arrays.asList(
                new Ability(1, "Java", "Java", List.of()),
                new Ability(2, "Spring", "Spring", List.of()),
                new Ability(3, "Reactive Programming", "Reactive Programming", List.of())
        );

        // Configure mocks for the chain of method calls
        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri("/ability/findAllByIds")).thenReturn(requestBodySpec);
        when(requestBodySpec.bodyValue(any(GetAbilitiesByIdsDtoRequest.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToFlux(Ability.class)).thenReturn(Flux.fromIterable(expectedAbilities));

        // Act
        Mono<List<Ability>> result = abilityClient.findAllAbilitiesByIds(abilityIds);

        // Assert
        StepVerifier.create(result)
                .expectNext(expectedAbilities)
                .verifyComplete();

        // Verify that the correct request object was sent
        verify(requestBodySpec).bodyValue(any(GetAbilitiesByIdsDtoRequest.class));
    }

    @Test
    void linkAbilitiesToBootcamp_ShouldCallApiCorrectly() {
        // Arrange
        Integer bootcampId = 1;
        List<Ability> abilities = Arrays.asList(
                new Ability(1, "Java", "Java", List.of()),
                new Ability(2, "Spring", "Spring", List.of())
        );

        // Configure mocks for the chain of method calls
        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri("/bootcamp/addBootcamp")).thenReturn(requestBodySpec);
        when(requestBodySpec.bodyValue(any(AddBootcampDtoRequest.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Void.class)).thenReturn(Mono.empty());

        // Act
        Mono<Void> result = abilityClient.linkAbilitiesToBootcamp(bootcampId, abilities);

        // Assert
        StepVerifier.create(result)
                .verifyComplete();

        // Verify the correct request object was created and sent
        verify(requestBodySpec).bodyValue(any(AddBootcampDtoRequest.class));
    }

    @Test
    void findAllAbilitiesByIds_ShouldHandleEmptyResponse() {
        // Arrange
        List<Integer> abilityIds = Arrays.asList(1, 2, 3);

        // Configure mocks for the chain of method calls
        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri("/ability/findAllByIds")).thenReturn(requestBodySpec);
        when(requestBodySpec.bodyValue(any(GetAbilitiesByIdsDtoRequest.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToFlux(Ability.class)).thenReturn(Flux.empty());

        // Act
        Mono<List<Ability>> result = abilityClient.findAllAbilitiesByIds(abilityIds);

        // Assert
        StepVerifier.create(result)
                .expectNext(List.of())
                .verifyComplete();
    }

    @Test
    void findAllAbilitiesByIds_ShouldPropagateError() {
        // Arrange
        List<Integer> abilityIds = Arrays.asList(1, 2, 3);
        RuntimeException expectedException = new RuntimeException("API Error");

        // Configure mocks for the chain of method calls
        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri("/ability/findAllByIds")).thenReturn(requestBodySpec);
        when(requestBodySpec.bodyValue(any(GetAbilitiesByIdsDtoRequest.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToFlux(Ability.class)).thenReturn(Flux.error(expectedException));

        // Act
        Mono<List<Ability>> result = abilityClient.findAllAbilitiesByIds(abilityIds);

        // Assert
        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable.equals(expectedException))
                .verify();
    }

    @Test
    void linkAbilitiesToBootcamp_ShouldPropagateError() {
        // Arrange
        Integer bootcampId = 1;
        List<Ability> abilities = Arrays.asList(
                new Ability(1, "Java","Java",List.of()),
                new Ability(2, "Spring","Spring",List.of())
        );
        RuntimeException expectedException = new RuntimeException("API Error");

        // Configure mocks for the chain of method calls
        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri("/bootcamp/addBootcamp")).thenReturn(requestBodySpec);
        when(requestBodySpec.bodyValue(any(AddBootcampDtoRequest.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Void.class)).thenReturn(Mono.error(expectedException));

        // Act
        Mono<Void> result = abilityClient.linkAbilitiesToBootcamp(bootcampId, abilities);

        // Assert
        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable.equals(expectedException))
                .verify();
    }
    @Test
    void getAllAbilitiesByBootcampId_ShouldReturnListOfAbilities() {
        Integer bootcampId = 1;
        List<Ability> expectedAbilities = Arrays.asList(
                new Ability(1, "Java", "Java", List.of()),
                new Ability(2, "Spring", "Spring", List.of())
        );

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri("/bootcamp/getAllAbilitiesByBootcampId?id={id}", bootcampId)).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToFlux(Ability.class)).thenReturn(Flux.fromIterable(expectedAbilities));

        Mono<List<Ability>> result = abilityClient.getAllAbilitiesByBootcampId(bootcampId);

        StepVerifier.create(result)
                .expectNext(expectedAbilities)
                .verifyComplete();
    }

    @Test
    void getAllAbilitiesByBootcampId_ShouldHandleEmptyResponse() {
        Integer bootcampId = 1;

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri("/bootcamp/getAllAbilitiesByBootcampId?id={id}", bootcampId)).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToFlux(Ability.class)).thenReturn(Flux.empty());

        Mono<List<Ability>> result = abilityClient.getAllAbilitiesByBootcampId(bootcampId);

        StepVerifier.create(result)
                .expectNext(List.of())
                .verifyComplete();
    }

}