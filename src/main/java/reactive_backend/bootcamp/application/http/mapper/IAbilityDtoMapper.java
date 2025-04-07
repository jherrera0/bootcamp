package reactive_backend.bootcamp.application.http.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import reactive_backend.bootcamp.application.http.dto.response.AbilityDtoResponse;
import reactive_backend.bootcamp.domain.model.Ability;

@Mapper(componentModel = "spring",
        uses = {ITechnologyDtoMapper.class},
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IAbilityDtoMapper {
    AbilityDtoResponse toDtoResponse(Ability ability);
}
