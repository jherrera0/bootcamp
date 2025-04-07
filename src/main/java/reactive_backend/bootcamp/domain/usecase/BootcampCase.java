package reactive_backend.bootcamp.domain.usecase;

import reactive_backend.bootcamp.domain.api.IBootcampServicePort;
import reactive_backend.bootcamp.domain.exception.*;
import reactive_backend.bootcamp.domain.model.Ability;
import reactive_backend.bootcamp.domain.model.Bootcamp;
import reactive_backend.bootcamp.domain.model.PageCustom;
import reactive_backend.bootcamp.domain.spi.IAbilityClientPort;
import reactive_backend.bootcamp.domain.spi.IBootcampPersistencePort;
import reactive_backend.bootcamp.domain.util.ConstValidation;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Comparator;
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

    @Override
    public Mono<PageCustom<Bootcamp>> getAllBootcamps(String orderDirection, String sortField, Integer pageSize, Integer currentPage) {
        Mono<PageCustom<Bootcamp>> error = validateParametersPage(orderDirection, sortField, pageSize, currentPage);
        if (error != null) return error;

        return bootcampPersistencePort.getAllBootcamps(pageSize, currentPage)
                .flatMap(bootcampPageCustom -> {
                    if (bootcampPageCustom.getTotalPages() < bootcampPageCustom.getCurrentPage() + ConstValidation.ONE)
                        return Mono.error(new ListBootcampPageInvalidException());

                    return Flux.fromIterable(bootcampPageCustom.getItems())
                            .flatMapSequential(bootcamp -> abilityClientPort.getAllAbilitiesByBootcampId(bootcamp.getId())
                                    .flatMap(abilities -> {
                                        bootcamp.setAbilities(abilities);
                                        return Mono.just(bootcamp);
                                    }))
                            .collectList()
                            .flatMap(bootcamps -> {
                                bootcampPageCustom.setItems(bootcamps);

                                if (sortField.equals(ConstValidation.NAME) || sortField.equals(ConstValidation.ABILITIES_SIZE))
                                    return sortByField(bootcamps, sortField, orderDirection)
                                            .flatMap(sorted -> {
                                                bootcampPageCustom.setItems(sorted);
                                                return Mono.just(bootcampPageCustom);
                                            });

                                return Mono.just(bootcampPageCustom);
                            });
                });
    }


    private Mono<List<Bootcamp>> sortByField(List<Bootcamp> bootcamps, String sortField, String orderDirection) {
        if (sortField.equals(ConstValidation.NAME)) {
            if (orderDirection.equals(ConstValidation.ASC)) {
                bootcamps.sort(Comparator.comparing(Bootcamp::getName));
            } else {
                bootcamps.sort((b1, b2) -> b2.getName().compareTo(b1.getName()));
            }
        } else if (sortField.equals(ConstValidation.ABILITIES_SIZE)) {
            if (orderDirection.equals(ConstValidation.ASC)) {
                bootcamps.sort(Comparator.comparingInt(b -> b.getAbilities().size()));
            } else {
                bootcamps.sort((b1, b2) -> Integer.compare(b2.getAbilities().size(), b1.getAbilities().size()));
            }
        }
        return Mono.just(bootcamps);
    }

    private Mono<PageCustom<Bootcamp>> validateParametersPage(String orderDirection, String sortField, Integer pageSize, Integer currentPage) {
        if(orderDirection.compareTo(ConstValidation.ASC) != ConstValidation.ZERO &&
                orderDirection.compareTo(ConstValidation.DESC) != ConstValidation.ZERO) {
            return Mono.error(new ListBootcampOrderDirectionInvalidException());
        }
        if (sortField.compareTo(ConstValidation.NAME) != ConstValidation.ZERO &&
                sortField.compareTo(ConstValidation.ABILITIES_SIZE) != ConstValidation.ZERO) {
            return Mono.error(new ListBootcampSortFieldInvalidException());
        }
        if (pageSize <= ConstValidation.ZERO) {
            return Mono.error(new ListBootcampPageSizeInvalidException());
        }

        if(currentPage < ConstValidation.ZERO) {
            return Mono.error(new ListBootcampPageCurrentInvalidException());
        }
        return null;
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
