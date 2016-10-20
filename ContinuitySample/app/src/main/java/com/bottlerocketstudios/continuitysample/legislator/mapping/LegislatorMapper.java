package com.bottlerocketstudios.continuitysample.legislator.mapping;

import com.bottlerocketstudios.continuitysample.legislator.model.Legislator;
import com.bottlerocketstudios.continuitysample.legislator.viewmodel.LegislatorViewModel;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

/**
 * Map Legislator related models to view models.
 */
@Mapper
public interface LegislatorMapper {
    LegislatorMapper INSTANCE = Mappers.getMapper(LegislatorMapper.class);

    void updateLegislatorViewModel(Legislator legislator, @MappingTarget LegislatorViewModel legislatorViewModel);
    LegislatorViewModel legislatorToViewModel(Legislator legislator);

}
