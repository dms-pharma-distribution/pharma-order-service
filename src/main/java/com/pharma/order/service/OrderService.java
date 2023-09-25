package com.pharma.order.service;

import java.util.UUID;

import com.pharma.order.dto.OrderCreateListDto;
import com.pharma.order.dto.OrderDto;
import com.pharma.order.dto.RatingDto;

public interface OrderService {
	
	public void createOrders(OrderCreateListDto orderCreateListDto);	
	
	public void updateOrder(OrderDto ordersDto,Long orderCode);
	
	public void orderSQSProducer(OrderDto orderEntity);
	
	public OrderDto findOrderByOrderCode(long orderCode);
	
	public OrderDto findOrderByOrderGuid(UUID orderGuid);
	
	//public  OrderQueryDto findByRatingId(long ratingId);
	
	public void createRatinForOrder(Long orderCode,RatingDto ratingDto);

}
