package rental.infrastructure.mapper;

import rental.domain.model.House;
import rental.infrastructure.dataentity.HouseEntity;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface EntityToModelMapper {

    EntityToModelMapper INSTANCE = Mappers.getMapper(EntityToModelMapper.class);

    House mapToModel(HouseEntity entity);

}
