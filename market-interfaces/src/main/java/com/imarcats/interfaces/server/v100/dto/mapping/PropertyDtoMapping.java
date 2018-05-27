package com.imarcats.interfaces.server.v100.dto.mapping;

import java.util.ArrayList;
import java.util.List;

import org.dozer.loader.api.BeanMappingBuilder;

import com.imarcats.interfaces.client.v100.dto.helpers.ActivationStatusWrapperDto;
import com.imarcats.interfaces.client.v100.dto.helpers.BooleanWrapperDto;
import com.imarcats.interfaces.client.v100.dto.helpers.ExecutionSystemWrapperDto;
import com.imarcats.interfaces.client.v100.dto.helpers.LongWrapperDto;
import com.imarcats.interfaces.client.v100.dto.helpers.MarketStateWrapperDto;
import com.imarcats.interfaces.client.v100.dto.helpers.OrderExpirationInstructionWrapperDto;
import com.imarcats.interfaces.client.v100.dto.helpers.OrderSideWrapperDto;
import com.imarcats.interfaces.client.v100.dto.helpers.OrderStateWrapperDto;
import com.imarcats.interfaces.client.v100.dto.helpers.OrderTriggerInstructionWrapperDto;
import com.imarcats.interfaces.client.v100.dto.helpers.OrderTypeWrapperDto;
import com.imarcats.interfaces.client.v100.dto.helpers.QuoteTypeWrapperDto;
import com.imarcats.interfaces.client.v100.dto.helpers.RecurringActionDetailWrapperDto;
import com.imarcats.interfaces.client.v100.dto.helpers.SecondaryOrderPrecedenceRuleTypeWrapperDto;
import com.imarcats.interfaces.client.v100.dto.helpers.TradingSessionWrapperDto;
import com.imarcats.interfaces.client.v100.dto.types.AuditInformationDto;
import com.imarcats.interfaces.client.v100.dto.types.BusinessCalendarDto;
import com.imarcats.interfaces.client.v100.dto.types.CircuitBreakerDto;
import com.imarcats.interfaces.client.v100.dto.types.PropertyChangeDto;
import com.imarcats.interfaces.client.v100.dto.types.PropertyListValueChangeDto;
import com.imarcats.interfaces.client.v100.dto.types.PropertyValueChangeDto;
import com.imarcats.interfaces.client.v100.dto.types.TimeOfDayDto;
import com.imarcats.interfaces.client.v100.dto.types.TimePeriodDto;
import com.imarcats.model.CircuitBreaker;
import com.imarcats.model.mutators.PropertyChange;
import com.imarcats.model.mutators.PropertyListValueChange;
import com.imarcats.model.mutators.PropertyValueChange;
import com.imarcats.model.mutators.helpers.ActivationStatusWrapper;
import com.imarcats.model.mutators.helpers.BooleanWrapper;
import com.imarcats.model.mutators.helpers.ExecutionSystemWrapper;
import com.imarcats.model.mutators.helpers.LongWrapper;
import com.imarcats.model.mutators.helpers.MarketStateWrapper;
import com.imarcats.model.mutators.helpers.OrderExpirationInstructionWrapper;
import com.imarcats.model.mutators.helpers.OrderSideWrapper;
import com.imarcats.model.mutators.helpers.OrderStateWrapper;
import com.imarcats.model.mutators.helpers.OrderTriggerInstructionWrapper;
import com.imarcats.model.mutators.helpers.OrderTypeWrapper;
import com.imarcats.model.mutators.helpers.QuoteTypeWrapper;
import com.imarcats.model.mutators.helpers.RecurringActionDetailWrapper;
import com.imarcats.model.mutators.helpers.SecondaryOrderPrecedenceRuleTypeWrapper;
import com.imarcats.model.mutators.helpers.TradingSessionWrapper;
import com.imarcats.model.types.AuditInformation;
import com.imarcats.model.types.BusinessCalendar;
import com.imarcats.model.types.TimeOfDay;
import com.imarcats.model.types.TimePeriod;

public class PropertyDtoMapping extends DtoMappingBase {
	public static PropertyDtoMapping INSTANCE = new PropertyDtoMapping();

	protected PropertyDtoMapping() {
		super();
		_mapper.addMapping(createObjectPropertyMappingBuilder());
	}
	
	public static BeanMappingBuilder createObjectPropertyMappingBuilder() {
		BeanMappingBuilder builder = new BeanMappingBuilder() {

			@Override
			protected void configure() {
				// common 
				mapping(AuditInformation.class, AuditInformationDto.class);
				mapping(BooleanWrapper.class, BooleanWrapperDto.class);
				
				// market 
				mapping(ActivationStatusWrapper.class, ActivationStatusWrapperDto.class);
				mapping(ExecutionSystemWrapper.class, ExecutionSystemWrapperDto.class);
				mapping(MarketStateWrapper.class, MarketStateWrapperDto.class);
				mapping(QuoteTypeWrapper.class, QuoteTypeWrapperDto.class);
				mapping(RecurringActionDetailWrapper.class, RecurringActionDetailWrapperDto.class);
				mapping(SecondaryOrderPrecedenceRuleTypeWrapper.class, SecondaryOrderPrecedenceRuleTypeWrapperDto.class);
				mapping(TradingSessionWrapper.class, TradingSessionWrapperDto.class);
								
				mapping(BusinessCalendar.class, BusinessCalendarDto.class);
				mapping(CircuitBreaker.class, CircuitBreakerDto.class);
				mapping(TimeOfDay.class, TimeOfDayDto.class);
				mapping(TimePeriod.class, TimePeriodDto.class);
				
				// order
				mapping(LongWrapper.class, LongWrapperDto.class);
				mapping(OrderExpirationInstructionWrapper.class, OrderExpirationInstructionWrapperDto.class);
				mapping(OrderSideWrapper.class, OrderSideWrapperDto.class);
				mapping(OrderStateWrapper.class, OrderStateWrapperDto.class);
				mapping(OrderTriggerInstructionWrapper.class, OrderTriggerInstructionWrapperDto.class);
				mapping(OrderTypeWrapper.class, OrderTypeWrapperDto.class);				
				
			}
			
		};
		
		return builder;
	}
	
	public PropertyChangeDto[] toDto(PropertyChange[] change_) {
		if(change_ == null) {
			return null;
		}
		List<PropertyChangeDto> dtos = new ArrayList<PropertyChangeDto>();
		for (PropertyChange propertyChange : change_) {
			dtos.add(toDto(propertyChange));
		}
		
		return dtos.toArray(new PropertyChangeDto[dtos.size()]);
	}
	
	public PropertyChange[] fromDto(PropertyChangeDto[] change_) {
		if(change_ == null) {
			return null;
		}
		List<PropertyChange> models = new ArrayList<PropertyChange>();
		for (PropertyChangeDto propertyChange : change_) {
			models.add(fromDto(propertyChange));
		}
		
		return models.toArray(new PropertyChange[models.size()]);
	}
	
	public PropertyChangeDto toDto(PropertyChange change_) {
		if(change_ == null) {
			return null;
		}
		if(change_ instanceof PropertyListValueChange) {
			return toDto((PropertyListValueChange)change_);
		} else if(change_ instanceof PropertyValueChange) {
			return toDto((PropertyValueChange)change_);
		} else {
			throw new IllegalArgumentException("Unsupported property change");
		}
	}

	public PropertyChange fromDto(PropertyChangeDto change_) {
		if(change_ == null) {
			return null;
		}
		if(change_ instanceof PropertyListValueChangeDto) {
			return fromDto((PropertyListValueChangeDto)change_);
		} else if(change_ instanceof PropertyValueChangeDto) {
			return fromDto((PropertyValueChangeDto)change_);
		} else {
			throw new IllegalArgumentException("Unsupported property change");
		}
	}
	
	public PropertyValueChangeDto toDto(PropertyValueChange change_) {
		return _mapper.map(change_, PropertyValueChangeDto.class);
	}
	
	public PropertyValueChange fromDto(PropertyValueChangeDto change_) {
		return _mapper.map(change_, PropertyValueChange.class);
	}
	
	public PropertyListValueChangeDto toDto(PropertyListValueChange change_) {
		return _mapper.map(change_, PropertyListValueChangeDto.class);
	}
	
	public PropertyListValueChange fromDto(PropertyListValueChangeDto change_) {
		return _mapper.map(change_, PropertyListValueChange.class);
	}
}
