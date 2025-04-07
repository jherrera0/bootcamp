package reactive_backend.bootcamp.application.http.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import reactive_backend.bootcamp.application.http.dto.request.CreateBootcampDtoRequest;
import reactive_backend.bootcamp.application.http.dto.response.BootcampDtoResponse;
import reactive_backend.bootcamp.domain.model.Ability;
import reactive_backend.bootcamp.domain.model.Bootcamp;

import java.util.Collections;
import java.util.List;

@Mapper(componentModel = "spring",
        uses = {IAbilityDtoMapper.class},
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface ICreateBootcampDtoMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "abilities", source = "abilitiesIds")
    Bootcamp toDomain(CreateBootcampDtoRequest dtoRequest);
    BootcampDtoResponse toDtoResponse(Bootcamp bootcamp);

    default List<Ability> map(List<Integer> abilitiesIds) {
      if (abilitiesIds == null) {
        return Collections.emptyList();
      }
      return abilitiesIds.stream().
              map(abilityId -> {
                  Ability ability = new Ability();
                  ability.setId(abilityId);
                  return ability;
              }).toList();
    }
}
