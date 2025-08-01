package com.momentum.minimomentum.mapper;


import com.momentum.minimomentum.dto.responseDTO.SummaryDetailsDTO;
import com.momentum.minimomentum.model.SummaryDetails;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface SummaryDetailsMapper {
    SummaryDetailsMapper INSTANCE = Mappers.getMapper(SummaryDetailsMapper.class);
    SummaryDetails toSummaryEntity(SummaryDetailsDTO dto);
    SummaryDetailsDTO toSummaryDto(SummaryDetails entity);
}
