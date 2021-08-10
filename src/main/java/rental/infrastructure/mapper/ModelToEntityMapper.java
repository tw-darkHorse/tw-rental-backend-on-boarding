package rental.infrastructure.mapper;

import org.mapstruct.factory.Mappers;
import rental.domain.model.House;
import rental.infrastructure.dataentity.HouseEntity;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ModelToEntityMapper {
    ModelToEntityMapper INSTANCE = Mappers.getMapper(ModelToEntityMapper.class);

    HouseEntity mapToEntity(House promotionProposal);

}
