package reactive_backend.bootcamp.application.client.adapter;

import org.springframework.web.reactive.function.client.WebClient;
import reactive_backend.bootcamp.application.http.dto.request.AddBootcampDtoRequest;
import reactive_backend.bootcamp.application.http.dto.request.GetAbilitiesByIdsDtoRequest;
import reactive_backend.bootcamp.domain.model.Ability;
import reactive_backend.bootcamp.domain.spi.IAbilityClientPort;
import reactive_backend.bootcamp.domain.util.ConstRoute;
import reactor.core.publisher.Mono;

import java.util.List;

public class AbilityClient implements IAbilityClientPort {
    private final WebClient webClient;

    public AbilityClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(ConstRoute.ABILITY_REST_ROUTE).build();
    }

    @Override
    public Mono<List<Ability>> findAllAbilitiesByIds(List<Integer> ids) {
        GetAbilitiesByIdsDtoRequest request = new GetAbilitiesByIdsDtoRequest(ids);
        return webClient.post()
                .uri("/ability/findAllByIds")
                .bodyValue(request)
                .retrieve()
                .bodyToFlux(Ability.class)
                .collectList();
    }

    @Override
    public Mono<Void> linkAbilitiesToBootcamp(Integer id, List<Ability> abilities) {
        AddBootcampDtoRequest request = new AddBootcampDtoRequest();
        request.setBootcampId(id);
        request.setAbilitiesIds(abilities);
        return webClient.post()
                .uri("/bootcamp/addBootcamp")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(Void.class)
                .then();

    }
}
