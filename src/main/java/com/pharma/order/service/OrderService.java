package com.pharma.order.service;

import java.util.UUID;

import com.pharma.order.dto.OrderDto;
import com.pharma.order.dto.OrderQueryDto;
import com.pharma.order.dto.OrderCreateListDto;

public interface OrderService {
	
	public void createOrders(OrderCreateListDto orderCreateListDto);
	
	/*
	 * public void updateOrder(OrderDto ordersDto,Long orderCode);
	 * 
	 * public void deleteOrder(long orderCode);
	 */
	
	public void orderSQSProducer(OrderDto orderEntity);
	
	public OrderQueryDto findOrderByOrderCode(long orderCode);
	
	public OrderQueryDto findOrderByOrderGuid(UUID orderGuid);	

}
