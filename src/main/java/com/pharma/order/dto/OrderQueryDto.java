package com.pharma.order.dto;

import java.io.Serializable;
import java.util.List;

public class OrderQueryDto implements Serializable {

	private static final long serialVersionUID = 2515033700689069526L;

	private OrderDto orderDto;
	private RatingDto ratingDto;
	private List<OrderHistoryDto> orderHistoryDtos;

	public OrderQueryDto() {
		super();
	}

	public OrderQueryDto(OrderDto orderDto, RatingDto ratingDto, List<OrderHistoryDto> orderHistoryDtos) {
		super();
		this.orderDto = orderDto;
		this.ratingDto = ratingDto;
		this.orderHistoryDtos = orderHistoryDtos;
	}

	public OrderDto getOrderDto() {
		return orderDto;
	}

	public void setOrderDto(OrderDto orderDto) {
		this.orderDto = orderDto;
	}

	public RatingDto getRatingDto() {
		return ratingDto;
	}

	public void setRatingDto(RatingDto ratingDto) {
		this.ratingDto = ratingDto;
	}

	public List<OrderHistoryDto> getOrderHistoryDtos() {
		return orderHistoryDtos;
	}

	public void setOrderHistoryDtos(List<OrderHistoryDto> orderHistoryDtos) {
		this.orderHistoryDtos = orderHistoryDtos;
	}

	@Override
	public String toString() {
		return "OrderQueryDto [orderDto=" + orderDto + ", ratingDto=" + ratingDto + ", orderHistoryDtos="
				+ orderHistoryDtos + "]";
	}	
}
