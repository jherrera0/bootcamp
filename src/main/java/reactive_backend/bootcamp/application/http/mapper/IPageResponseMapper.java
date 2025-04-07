package reactive_backend.bootcamp.application.http.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import reactive_backend.bootcamp.application.http.dto.response.BootcampDtoResponse;
import reactive_backend.bootcamp.application.http.dto.response.PageResponse;
import reactive_backend.bootcamp.domain.model.Bootcamp;
import reactive_backend.bootcamp.domain.model.PageCustom;

@Mapper(componentModel = "spring",
        uses = {ICreateBootcampDtoMapper.class},
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IPageResponseMapper {
    @Mapping(target = "currentPage", source = "currentPage")
    @Mapping(target = "pageSize", source = "pageSize")
    @Mapping(target = "totalPages", source = "totalPages")
    @Mapping(target = "items", source = "items")
    PageResponse<BootcampDtoResponse> toPageResponse(PageCustom<Bootcamp> pageCustom);
}
