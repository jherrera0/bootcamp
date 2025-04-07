package reactive_backend.bootcamp.domain.spi;

import reactive_backend.bootcamp.domain.model.Ability;
import reactor.core.publisher.Mono;

import java.util.List;

public interface IAbilityClientPort {
    Mono<List<Ability>> findAllAbilitiesByIds(List<Integer> ids);

     Mono<Void> linkAbilitiesToBootcamp(Integer id, List<Ability> abilities);
}
