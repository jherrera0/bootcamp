package reactive_backend.bootcamp.domain.usecase;

import reactive_backend.bootcamp.domain.api.IBootcampServicePort;
import reactive_backend.bootcamp.domain.exception.*;
import reactive_backend.bootcamp.domain.model.Ability;
import reactive_backend.bootcamp.domain.model.Bootcamp;
import reactive_backend.bootcamp.domain.spi.IAbilityClientPort;
import reactive_backend.bootcamp.domain.spi.IBootcampPersistencePort;
import reactive_backend.bootcamp.domain.util.ConstValidation;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class BootcampCase implements IBootcampServicePort {
    private final IBootcampPersistencePort bootcampPersistencePort;
    private final IAbilityClientPort abilityClientPort;

    public BootcampCase(IBootcampPersistencePort bootcampPersistencePort,
                        IAbilityClientPort abilityClientPort) {
        this.bootcampPersistencePort = bootcampPersistencePort;
        this.abilityClientPort = abilityClientPort;
    }
    @Override
    public Mono<Bootcamp> createBootcamp(Bootcamp bootcamp) {
        Mono<Bootcamp> error = validateParameters(bootcamp);
        if (error != null) return error;
        List<Integer> abilities = bootcamp.getAbilities().stream()
                .map(Ability::getId)
                .toList();
        if (hasDuplicatedAbilities(abilities)) {
            return Mono.error(new BootcampAbilitiesDuplicatedException());
        }

        return abilityClientPort.findAllAbilitiesByIds(abilities)
                .flatMap(abilitiesList -> {
                    Set<Integer> foundAbilityIds = abilitiesList.stream()
                            .map(Ability::getId)
                            .collect(Collectors.toSet());

                    List<Integer> notFoundAbilities = abilities.stream()
                            .filter(abilityId -> !foundAbilityIds.contains(abilityId))
                            .toList();

                    if (!notFoundAbilities.isEmpty()) {
                        return Mono.error(new BootcampAbilitiesNotFoundException(notFoundAbilities));
                    }
                    return bootcampPersistencePort.createBootcamp(bootcamp)
                            .flatMap(savedBootcamp ->
                                    abilityClientPort.linkAbilitiesToBootcamp(savedBootcamp.getId(), abilitiesList)
                                            .then(Mono.fromCallable(() -> {
                                                savedBootcamp.setAbilities(abilitiesList);
                                                return savedBootcamp;
                                            })));
                });
    }

    private boolean hasDuplicatedAbilities(List<Integer> abilities) {
        return abilities.size() != abilities.stream().distinct().count();
    }

    private Mono<Bootcamp> validateParameters(Bootcamp bootcamp) {
        if(bootcamp.getName().isBlank()){
            return Mono.error(new BootcampNameEmptyException());
        }
        if (bootcamp.getDescription().isBlank()){
            return Mono.error(new BootcampDescriptionEmptyException());
        }
        if (bootcamp.getAbilities().size()< ConstValidation.BOOTCAMP_ABILITIES_MIN_SIZE ||
                bootcamp.getAbilities().size()>ConstValidation.BOOTCAMP_ABILITIES_MAX_SIZE){
            return Mono.error(new BootcampInvalidAbilitiesSizeException());
        }
        return null;
    }
}
