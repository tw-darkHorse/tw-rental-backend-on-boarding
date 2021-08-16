package rental.infrastructure.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import rental.client.dto.request.HouseDto;
import rental.domain.model.House;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ModelToClientDtoMapper {

    ModelToClientDtoMapper INSTANCE = Mappers.getMapper(ModelToClientDtoMapper.class);

    HouseDto mapToDto(House model);
}
