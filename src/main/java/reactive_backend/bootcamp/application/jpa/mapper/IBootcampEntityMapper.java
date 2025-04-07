package reactive_backend.bootcamp.application.jpa.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import reactive_backend.bootcamp.application.jpa.entity.BootcampEntity;
import reactive_backend.bootcamp.domain.model.Bootcamp;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IBootcampEntityMapper {

    Bootcamp toDomain(BootcampEntity entity);

    @Mapping(target = "id", ignore = true)
    BootcampEntity toEntity(Bootcamp domain);
}
