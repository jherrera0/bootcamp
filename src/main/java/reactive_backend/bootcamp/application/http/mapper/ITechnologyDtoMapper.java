package reactive_backend.bootcamp.application.http.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import reactive_backend.bootcamp.application.http.dto.response.TechnologyDtoResponse;
import reactive_backend.bootcamp.domain.model.Technology;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface ITechnologyDtoMapper {
    @Mapping(target = "id", source = "id")
    TechnologyDtoResponse toDtoResponse(Technology technology);
}
