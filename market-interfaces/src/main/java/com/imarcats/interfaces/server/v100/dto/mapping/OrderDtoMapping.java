package com.imarcats.interfaces.server.v100.dto.mapping;

import com.imarcats.interfaces.client.v100.dto.OrderDto;
import com.imarcats.interfaces.client.v100.dto.types.OrderSide;
import com.imarcats.interfaces.client.v100.dto.types.PagedOrderListDto;
import com.imarcats.model.Order;
import com.imarcats.model.types.PagedOrderList;

public class OrderDtoMapping extends DtoMappingBase {
	public static OrderDtoMapping INSTANCE = new OrderDtoMapping();
	
	public OrderDto toDto(Order order_) {
		return _mapper.map(order_, OrderDto.class);
	}
	
	public Order fromDto(OrderDto order_) {
		return _mapper.map(order_, Order.class);
	}
	
	public PagedOrderListDto toDto(PagedOrderList userList_) {
		return _mapper.map(userList_, PagedOrderListDto.class);
	}
	
	public PagedOrderList fromDto(PagedOrderListDto userList_) {
		return _mapper.map(userList_, PagedOrderList.class);
	}
	
	public OrderSide toDto(com.imarcats.model.types.OrderSide side_) {
		return side_ != null ? OrderSide.valueOf(side_.name()) : null;
	}
	
	public com.imarcats.model.types.OrderSide fromDto(OrderSide membershipType_) {
		return membershipType_ != null ? com.imarcats.model.types.OrderSide.valueOf(membershipType_.name()) : null;
	}
}
