package com.pharma.order.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.pharma.order.dto.ItemDto;
import com.pharma.order.dto.OrderDto;
import com.pharma.order.dto.OrderHistoryDto;
import com.pharma.order.dto.OrderQueryDto;
import com.pharma.order.dto.OrderStatusDto;
import com.pharma.order.dto.RatingDto;
import com.pharma.order.dto.SupplierRetailerDto;
import com.pharma.order.entity.ItemEntity;
import com.pharma.order.entity.OrderEntity;
import com.pharma.order.entity.OrderHistoryEntity;
import com.pharma.order.entity.OrderStatusEntity;
import com.pharma.order.entity.RatingEntity;
import com.pharma.order.entity.SupplierRetailerEntity;

@Component
public class OrderQueryMapper {

	OrderMapper orderMapper;

	public OrderQueryMapper(OrderMapper orderMapper) {
		this.orderMapper = orderMapper;
	}

	public OrderQueryDto mapToQueryDto(OrderEntity orderEntity, List<OrderHistoryEntity> orderHistoryEntities,
			RatingEntity ratingEntity) {
		var orderQueryDto = new OrderQueryDto();
		orderQueryDto.setOrderDto(mapToOrderDto(orderEntity));
		orderQueryDto.setOrderHistoryDtos(mapToOrderHistoryDtos(orderHistoryEntities));
		orderQueryDto.setRatingDto(mapToRatingDto(ratingEntity));
		return orderQueryDto;
	}

	public OrderDto mapToOrderDto(OrderEntity orderEntity) {
		var orderDto = orderMapper.toOrderDto(orderEntity);
		orderDto.setItemDtos(orderEntity.getItemEntities().stream().map(itemEntity -> mapToItemDto(itemEntity))
				.collect(Collectors.toList()));
		orderDto.setOrderStatusDto(mapToOrderStatusDto(orderEntity.getOrderStatusEntity()));
		orderDto.setSupplierRetailerDto(mapToSupplierRetailerDto(orderEntity.getSupplierRetailerEntity()));
		return orderDto;
	}

	public ItemDto mapToItemDto(ItemEntity itemEntity) {
		return orderMapper.toItemDto(itemEntity);
	}

	public OrderStatusDto mapToOrderStatusDto(OrderStatusEntity orderStatusEntity) {
		return orderMapper.toOrderStatusDto(orderStatusEntity);
	}

	public SupplierRetailerDto mapToSupplierRetailerDto(SupplierRetailerEntity supplierRetailerEntity) {
		return orderMapper.toSupplierRetailerDto(supplierRetailerEntity);
	}

	public RatingDto mapToRatingDto(RatingEntity ratingEntity) {
		return orderMapper.toRatingDto(ratingEntity);
	}

	public List<OrderHistoryDto> mapToOrderHistoryDtos(List<OrderHistoryEntity> orderHistoryEntities){
		return orderHistoryEntities.stream().map(orderHistoryEntity -> mapToOrderHistoryDto(orderHistoryEntity)).collect(Collectors.toList());
	}
	public OrderHistoryDto mapToOrderHistoryDto(OrderHistoryEntity orderHistoryEntity) {
		return orderMapper.toOrderHistoryDto(orderHistoryEntity);
		
	}

}
