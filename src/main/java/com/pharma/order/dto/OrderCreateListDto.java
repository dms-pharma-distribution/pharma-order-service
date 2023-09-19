package com.pharma.order.dto;

import java.io.Serializable;
import java.util.Set;

public class OrderCreateListDto implements Serializable{

	private static final long serialVersionUID = 470834178256500135L;
	
	Set<OrderDto> orderDtos;
	
	public OrderCreateListDto(){}

	public OrderCreateListDto(Set<OrderDto> orderDtos) {
		this.orderDtos = orderDtos;
	}

	public Set<OrderDto> getOrderDtos() {
		return orderDtos;
	}

	public void setOrderDtos(Set<OrderDto> orderDtos) {
		this.orderDtos = orderDtos;
	}	
	
}
