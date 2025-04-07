package reactive_backend.bootcamp.application.http.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactive_backend.bootcamp.application.http.dto.request.CreateBootcampDtoRequest;
import reactive_backend.bootcamp.application.http.dto.response.AbilityDtoResponse;
import reactive_backend.bootcamp.application.http.dto.response.BootcampDtoResponse;
import reactive_backend.bootcamp.domain.model.Ability;
import reactive_backend.bootcamp.domain.model.Bootcamp;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class ICreateBootcampDtoMapperTest {

    @Mock
    private IAbilityDtoMapper iAbilityDtoMapper;

    @InjectMocks
    private ICreateBootcampDtoMapperImpl mapper;

    AutoCloseable closeable;
    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @Test
    void mapToDomainWithValidRequest() {
        CreateBootcampDtoRequest request = new CreateBootcampDtoRequest();
        request.setAbilitiesIds(List.of(1, 2, 3));

        Bootcamp bootcamp = mapper.toDomain(request);

        assertEquals(3, bootcamp.getAbilities().size());
        assertTrue(bootcamp.getAbilities().stream().anyMatch(ability -> ability.getId() == 1));
        assertTrue(bootcamp.getAbilities().stream().anyMatch(ability -> ability.getId() == 2));
        assertTrue(bootcamp.getAbilities().stream().anyMatch(ability -> ability.getId() == 3));
    }

    @Test
    void mapToDomainWithNullAbilities() {
        CreateBootcampDtoRequest request = new CreateBootcampDtoRequest();
        request.setAbilitiesIds(null);

        Bootcamp bootcamp = mapper.toDomain(request);

        assertTrue(bootcamp.getAbilities().isEmpty());
    }

    @Test
    void mapToDtoResponseWithValidBootcamp() {
        Bootcamp bootcamp = new Bootcamp();
        bootcamp.setId(1);
        bootcamp.setAbilities(List.of(new Ability(1, "", "", List.of())));

        when(iAbilityDtoMapper.toDtoResponse(any(Ability.class))).thenReturn(new AbilityDtoResponse());

        BootcampDtoResponse response = mapper.toDtoResponse(bootcamp);

        assertEquals(1, response.getId());
        assertEquals(1, response.getAbilities().size());
    }
}