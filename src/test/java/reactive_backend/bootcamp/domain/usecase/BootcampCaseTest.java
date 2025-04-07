package reactive_backend.bootcamp.domain.usecase;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactive_backend.bootcamp.domain.exception.*;
import reactive_backend.bootcamp.domain.model.Ability;
import reactive_backend.bootcamp.domain.model.Bootcamp;
import reactive_backend.bootcamp.domain.model.PageCustom;
import reactive_backend.bootcamp.domain.spi.IAbilityClientPort;
import reactive_backend.bootcamp.domain.spi.IBootcampPersistencePort;
import reactive_backend.bootcamp.domain.util.ConstValidation;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class BootcampCaseTest {

    @Mock
    private IBootcampPersistencePort bootcampPersistencePort;

    @Mock
    private IAbilityClientPort abilityClientPort;

    private BootcampCase bootcampCase;

    AutoCloseable closeable;
    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        bootcampCase = new BootcampCase(bootcampPersistencePort, abilityClientPort);
    }

    @Test
    void createBootcamp_Success() {
        // Arrange
        Bootcamp inputBootcamp = createValidBootcamp();
        Bootcamp savedBootcamp = createValidBootcamp();
        savedBootcamp.setId(1);

        List<Ability> abilities = Arrays.asList(
                new Ability(1, "Java", "Java", List.of()),
                new Ability(2, "Spring", "Spring", List.of())
        );

        when(abilityClientPort.findAllAbilitiesByIds(anyList())).thenReturn(Mono.just(abilities));
        when(bootcampPersistencePort.createBootcamp(any(Bootcamp.class))).thenReturn(Mono.just(savedBootcamp));
        when(abilityClientPort.linkAbilitiesToBootcamp(any(Integer.class), anyList())).thenReturn(Mono.empty());

        // Act
        Mono<Bootcamp> result = bootcampCase.createBootcamp(inputBootcamp);

        // Assert
        StepVerifier.create(result)
                .expectNextMatches(bootcamp ->
                        bootcamp.getId() == 1 &&
                                bootcamp.getName().equals("Java Bootcamp") &&
                                bootcamp.getDescription().equals("Learn Java programming") &&
                                bootcamp.getAbilities().size() == 2 &&
                                bootcamp.getAbilities().get(0).getId() == 1 &&
                                bootcamp.getAbilities().get(1).getId() == 2
                )
                .verifyComplete();

        // Verify interactions
        verify(abilityClientPort).findAllAbilitiesByIds(anyList());
        verify(bootcampPersistencePort).createBootcamp(any(Bootcamp.class));
        verify(abilityClientPort).linkAbilitiesToBootcamp(any(Integer.class), anyList());
    }

    @Test
    void createBootcamp_EmptyName_ShouldFail() {
        // Arrange
        Bootcamp bootcamp = createValidBootcamp();
        bootcamp.setName("");

        // Act
        Mono<Bootcamp> result = bootcampCase.createBootcamp(bootcamp);

        // Assert
        StepVerifier.create(result)
                .expectError(BootcampNameEmptyException.class)
                .verify();
    }

    @Test
    void createBootcamp_EmptyDescription_ShouldFail() {
        // Arrange
        Bootcamp bootcamp = createValidBootcamp();
        bootcamp.setDescription("");

        // Act
        Mono<Bootcamp> result = bootcampCase.createBootcamp(bootcamp);

        // Assert
        StepVerifier.create(result)
                .expectError(BootcampDescriptionEmptyException.class)
                .verify();
    }

    @Test
    void createBootcamp_TooFewAbilities_ShouldFail() {
        // Arrange
        Bootcamp bootcamp = createValidBootcamp();
        bootcamp.setAbilities(Collections.emptyList());

        // Act
        Mono<Bootcamp> result = bootcampCase.createBootcamp(bootcamp);

        // Assert
        StepVerifier.create(result)
                .expectError(BootcampInvalidAbilitiesSizeException.class)
                .verify();
    }

    @Test
    void createBootcamp_TooManyAbilities_ShouldFail() {
        // Arrange
        Bootcamp bootcamp = createValidBootcamp();
        // Create a list with too many abilities
        List<Ability> tooManyAbilities = Arrays.asList(
                new Ability(1, "Java", "Java", List.of()),
                new Ability(2, "Spring", "Spring", List.of()),
                new Ability(3, "React", "React", List.of()),
                new Ability(4, "Node", "Node", List.of()),
                new Ability(5, "Python", "Python", List.of()),
                new Ability(6, "Go", "Go", List.of())
        );
        bootcamp.setAbilities(tooManyAbilities);

        // Act
        Mono<Bootcamp> result = bootcampCase.createBootcamp(bootcamp);

        // Assert
        StepVerifier.create(result)
                .expectError(BootcampInvalidAbilitiesSizeException.class)
                .verify();
    }

    @Test
    void createBootcamp_DuplicatedAbilities_ShouldFail() {
        // Arrange
        Bootcamp bootcamp = createValidBootcamp();
        List<Ability> duplicatedAbilities = Arrays.asList(
                new Ability(1, "Java","Java", List.of()),
                new Ability(1, "Java","Java",List.of()) // Duplicate ID
        );
        bootcamp.setAbilities(duplicatedAbilities);

        // Act
        Mono<Bootcamp> result = bootcampCase.createBootcamp(bootcamp);

        // Assert
        StepVerifier.create(result)
                .expectError(BootcampAbilitiesDuplicatedException.class)
                .verify();
    }

    @Test
    void createBootcamp_AbilitiesNotFound_ShouldFail() {
        // Arrange
        Bootcamp bootcamp = createValidBootcamp();

        // Only one ability found, the other one is not found
        List<Ability> foundAbilities = List.of(new Ability(1, "Java", "Java programming", List.of()));

        when(abilityClientPort.findAllAbilitiesByIds(anyList())).thenReturn(Mono.just(foundAbilities));

        // Act
        Mono<Bootcamp> result = bootcampCase.createBootcamp(bootcamp);

        // Assert
        StepVerifier.create(result)
                .expectError(BootcampAbilitiesNotFoundException.class)
                .verify();

        verify(abilityClientPort).findAllAbilitiesByIds(anyList());
    }

    @Test
    void createBootcamp_PersistenceError_ShouldPropagateError() {
        // Arrange
        Bootcamp bootcamp = createValidBootcamp();
        RuntimeException expectedError = new RuntimeException("Database error");

        List<Ability> abilities = Arrays.asList(
                new Ability(1, "Java","Java", List.of()),
                new Ability(2, "Spring","Spring", List.of())
        );

        when(abilityClientPort.findAllAbilitiesByIds(anyList())).thenReturn(Mono.just(abilities));
        when(bootcampPersistencePort.createBootcamp(any(Bootcamp.class))).thenReturn(Mono.error(expectedError));

        // Act
        Mono<Bootcamp> result = bootcampCase.createBootcamp(bootcamp);

        // Assert
        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable.equals(expectedError))
                .verify();

        verify(abilityClientPort).findAllAbilitiesByIds(anyList());
        verify(bootcampPersistencePort).createBootcamp(any(Bootcamp.class));
    }

    @Test
    void createBootcamp_LinkingError_ShouldPropagateError() {
        // Arrange
        Bootcamp inputBootcamp = createValidBootcamp();
        Bootcamp savedBootcamp = createValidBootcamp();
        savedBootcamp.setId(1);

        List<Ability> abilities = Arrays.asList(
                new Ability(1, "Java","Java programming", List.of()),
                new Ability(2, "Spring","Spring framework", List.of())
        );

        RuntimeException expectedError = new RuntimeException("Linking error");

        when(abilityClientPort.findAllAbilitiesByIds(anyList())).thenReturn(Mono.just(abilities));
        when(bootcampPersistencePort.createBootcamp(any(Bootcamp.class))).thenReturn(Mono.just(savedBootcamp));
        when(abilityClientPort.linkAbilitiesToBootcamp(any(Integer.class), anyList())).thenReturn(Mono.error(expectedError));

        // Act
        Mono<Bootcamp> result = bootcampCase.createBootcamp(inputBootcamp);

        // Assert
        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable.equals(expectedError))
                .verify();

        verify(abilityClientPort).findAllAbilitiesByIds(anyList());
        verify(bootcampPersistencePort).createBootcamp(any(Bootcamp.class));
        verify(abilityClientPort).linkAbilitiesToBootcamp(any(Integer.class), anyList());
    }

    @Test
    void getAllBootcampsWithAscAndName(){
        PageCustom<Bootcamp> pageCustom = new PageCustom<>(0, 10, 1,
                List.of(new Bootcamp(1,"Java Bootcamp", "Learn Java programming",
                        List.of(new Ability(1, "Java", "Java", List.of())))));
        when(bootcampPersistencePort.getAllBootcamps(10, 0)).thenReturn(Mono.just(pageCustom));
        when(abilityClientPort.getAllAbilitiesByBootcampId(anyInt()))
                .thenReturn(Mono.just(List.of(new Ability(1, "Java", "Java", List.of()))));

        StepVerifier.create(bootcampCase.getAllBootcamps(
                        ConstValidation.ASC,
                        ConstValidation.NAME,
                        10,
                        0))
                .expectNextMatches(result->
                        result.getItems().get(0).getName().equals("Java Bootcamp"))
                .verifyComplete();
    }

    @Test
    void getAllBootcampsWithDescAndName(){
        PageCustom<Bootcamp> pageCustom = new PageCustom<>(0, 10, 1,
                List.of(new Bootcamp(1,"Java Bootcamp", "Learn Java programming",
                        List.of(new Ability(1, "Java", "Java", List.of())))));
        when(bootcampPersistencePort.getAllBootcamps(10, 0)).thenReturn(Mono.just(pageCustom));
        when(abilityClientPort.getAllAbilitiesByBootcampId(anyInt()))
                .thenReturn(Mono.just(List.of(new Ability(1, "Java", "Java", List.of()))));

        StepVerifier.create(bootcampCase.getAllBootcamps(
                        ConstValidation.DESC,
                        ConstValidation.NAME,
                        10,
                        0))
                .expectNextMatches(result->
                        result.getItems().get(0).getName().equals("Java Bootcamp"))
                .verifyComplete();
    }
    @Test
    void getAllBootcampsWithAscAndAbilitiesSize(){
        PageCustom<Bootcamp> pageCustom = new PageCustom<>(0, 10, 1,
                List.of(new Bootcamp(1,"Java Bootcamp", "Learn Java programming",
                        List.of(new Ability(1, "Java", "Java", List.of())))));
        when(bootcampPersistencePort.getAllBootcamps(10, 0)).thenReturn(Mono.just(pageCustom));
        when(abilityClientPort.getAllAbilitiesByBootcampId(anyInt()))
                .thenReturn(Mono.just(List.of(new Ability(1, "Java", "Java", List.of()))));

        StepVerifier.create(bootcampCase.getAllBootcamps(
                        ConstValidation.ASC,
                        ConstValidation.ABILITIES_SIZE,
                        10,
                        0))
                .expectNextMatches(result->
                        result.getItems().get(0).getName().equals("Java Bootcamp"))
                .verifyComplete();
    }

    @Test
    void getAllBootcampsWithDescAndAbilitiesSize(){
        PageCustom<Bootcamp> pageCustom = new PageCustom<>(0, 10, 1,
                List.of(new Bootcamp(1,"Java Bootcamp", "Learn Java programming",
                        List.of(new Ability(1, "Java", "Java", List.of())))));
        when(bootcampPersistencePort.getAllBootcamps(10, 0)).thenReturn(Mono.just(pageCustom));
        when(abilityClientPort.getAllAbilitiesByBootcampId(anyInt()))
                .thenReturn(Mono.just(List.of(new Ability(1, "Java", "Java", List.of()))));

        StepVerifier.create(bootcampCase.getAllBootcamps(
                        ConstValidation.DESC,
                        ConstValidation.ABILITIES_SIZE,
                        10,
                        0))
                .expectNextMatches(result->
                        result.getItems().get(0).getName().equals("Java Bootcamp"))
                .verifyComplete();
    }

    @Test
    void getAllBootcampsWithInvalidOrderDirection() {
        StepVerifier.create(bootcampCase.getAllBootcamps("invalid", "name", 10, 1))
                .expectError(ListBootcampOrderDirectionInvalidException.class)
                .verify();
    }

    @Test
    void getAllBootcampsWithInvalidSortField() {
        StepVerifier.create(bootcampCase.getAllBootcamps("asc", "invalid", 10, 1))
                .expectError(ListBootcampSortFieldInvalidException.class)
                .verify();
    }

    @Test
    void getAllBootcampsWithInvalidPageSize() {
        StepVerifier.create(bootcampCase.getAllBootcamps("asc", "name", 0, 1))
                .expectError(ListBootcampPageSizeInvalidException.class)
                .verify();
    }

    @Test
    void getAllBootcampsWithInvalidCurrentPage() {
        StepVerifier.create(bootcampCase.getAllBootcamps("asc", "name", 10, -1))
                .expectError(ListBootcampPageCurrentInvalidException.class)
                .verify();
    }

    @Test
    void getAllBootcampsWithPageExceedingTotalPages() {
        PageCustom<Bootcamp> pageCustom = new PageCustom<>(2, 10, 1, List.of());
        when(bootcampPersistencePort.getAllBootcamps(10, 2)).thenReturn(Mono.just(pageCustom));

        StepVerifier.create(bootcampCase.getAllBootcamps("asc", "name", 10, 2))
                .expectError(ListBootcampPageInvalidException.class)
                .verify();
    }

    private Bootcamp createValidBootcamp() {
        Bootcamp bootcamp = new Bootcamp();
        bootcamp.setName("Java Bootcamp");
        bootcamp.setDescription("Learn Java programming");
        bootcamp.setAbilities(Arrays.asList(
                new Ability(1, "Java","",List.of()),
                new Ability(2, "Spring","",List.of())
        ));
        return bootcamp;
    }
}