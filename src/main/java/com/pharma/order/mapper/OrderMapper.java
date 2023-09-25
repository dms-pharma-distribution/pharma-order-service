package com.pharma.order.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.pharma.order.dto.ItemDto;
import com.pharma.order.dto.OrderDto;
import com.pharma.order.dto.OrderHistoryDto;
import com.pharma.order.dto.OrderStatusDto;
import com.pharma.order.dto.RatingDto;
import com.pharma.order.dto.SupplierRetailerDto;
import com.pharma.order.entity.ItemEntity;
import com.pharma.order.entity.OrderEntity;
import com.pharma.order.entity.OrderHistoryEntity;
import com.pharma.order.entity.OrderStatusEntity;
import com.pharma.order.entity.RatingEntity;
import com.pharma.order.entity.SupplierRetailerEntity;

@Mapper(componentModel = "spring")
public interface OrderMapper {

	@Mapping(source = "itemEntities", target = "itemDtos")
	@Mapping(source = "supplierRetailerEntity", target = "supplierRetailerDto")
	@Mapping(source = "orderStatusEntity", target = "orderStatusDto")
	OrderDto toOrderDto(OrderEntity orderEntity);

	@Mapping(source = "orderEntity.orderGuid", target = "orderGuid")
	ItemDto toItemDto(ItemEntity itemEntity);

	@Mapping(source = "orderEntity.orderGuid", target = "orderGuid")
	OrderHistoryDto toOrderHistoryDto(OrderHistoryEntity orderHistoryEntity);

	OrderStatusDto toOrderStatusDto(OrderStatusEntity orderStatusEntity);

	SupplierRetailerDto toSupplierRetailerDto(SupplierRetailerEntity SupplierRetailerEntity);

	RatingDto toRatingDto(RatingEntity ratingEntity);

	RatingEntity toRatingEntity(RatingDto ratingDto);

	default Long generateCodeIfNotExists() {
		long min = 100_000L;
		long max = 999_999L;
		return min + (long) (Math.random() * (max - min + 1));
	}

}
