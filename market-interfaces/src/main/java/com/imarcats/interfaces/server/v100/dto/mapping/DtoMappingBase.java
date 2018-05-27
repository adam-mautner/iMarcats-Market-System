package com.imarcats.interfaces.server.v100.dto.mapping;

import org.dozer.DozerBeanMapper;
import org.dozer.loader.api.BeanMappingBuilder;

import com.imarcats.interfaces.client.v100.dto.types.ActivationStatus;
import com.imarcats.interfaces.client.v100.dto.types.BooleanPropertyDto;
import com.imarcats.interfaces.client.v100.dto.types.DatePropertyDto;
import com.imarcats.interfaces.client.v100.dto.types.DateRangePropertyDto;
import com.imarcats.interfaces.client.v100.dto.types.DoublePropertyDto;
import com.imarcats.interfaces.client.v100.dto.types.IntPropertyDto;
import com.imarcats.interfaces.client.v100.dto.types.ObjectPropertyDto;
import com.imarcats.interfaces.client.v100.dto.types.StringListPropertyDto;
import com.imarcats.interfaces.client.v100.dto.types.StringPropertyDto;
import com.imarcats.interfaces.client.v100.dto.types.TimePropertyDto;
import com.imarcats.interfaces.client.v100.dto.types.TimeRangePropertyDto;
import com.imarcats.interfaces.client.v100.dto.types.UnitPropertyDto;
import com.imarcats.model.types.BooleanProperty;
import com.imarcats.model.types.DateProperty;
import com.imarcats.model.types.DateRangeProperty;
import com.imarcats.model.types.DoubleProperty;
import com.imarcats.model.types.IntProperty;
import com.imarcats.model.types.ObjectProperty;
import com.imarcats.model.types.StringListProperty;
import com.imarcats.model.types.StringProperty;
import com.imarcats.model.types.TimeProperty;
import com.imarcats.model.types.TimeRangeProperty;
import com.imarcats.model.types.UnitProperty;

public abstract class DtoMappingBase {

	protected final DozerBeanMapper _mapper;
	
	protected DtoMappingBase() {
		super();
		_mapper = new DozerBeanMapper();
		_mapper.addMapping(createPropertyMappingBuilder());
	}
	
	public static BeanMappingBuilder createPropertyMappingBuilder() {
		BeanMappingBuilder builder = new BeanMappingBuilder() {

			@Override
			protected void configure() {
				mapping(IntProperty.class, IntPropertyDto.class);
				mapping(BooleanProperty.class, BooleanPropertyDto.class);
				mapping(DateProperty.class, DatePropertyDto.class);
				mapping(DateRangeProperty.class, DateRangePropertyDto.class);
				mapping(DoubleProperty.class, DoublePropertyDto.class);
				mapping(ObjectProperty.class, ObjectPropertyDto.class);
				mapping(StringListProperty.class, StringListPropertyDto.class);
				mapping(StringProperty.class, StringPropertyDto.class);
				mapping(TimeProperty.class, TimePropertyDto.class);
				mapping(TimeRangeProperty.class, TimeRangePropertyDto.class);
				mapping(UnitProperty.class, UnitPropertyDto.class);
			}
			
		};
		
		return builder;
	}
	
	public ActivationStatus toDto(com.imarcats.model.types.ActivationStatus activationStatus_) {
		return activationStatus_ != null ? ActivationStatus.valueOf(activationStatus_.name()) : null;
	}
	
	public com.imarcats.model.types.ActivationStatus fromDto(ActivationStatus activationStatus_) {
		return activationStatus_ != null ? com.imarcats.model.types.ActivationStatus.valueOf(activationStatus_.name()) : null;
	}
}
