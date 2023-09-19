package com.pharma.order.mapper;

import java.sql.Date;
import java.util.UUID;

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

	//@Mapping(target = "createdDate", expression = "java(convertUtilDateToSqlDate(orderEntity.getCreatedDate()))")
	@Mapping(target = "orderCode", expression = "java(generateCodeIfNotExists(orderEntity.getOrderCode()))")
	@Mapping(target = "orderGuid", expression = "java(generateOrderGuidIfNotExists(orderEntity.getOrderGuid()))")
	OrderEntity toOrderEntity(OrderDto orderDto);

	@Mapping(target = "itemId", expression = "java(generateCodeIfNotExists(itemEntity.getItemId()))")
	ItemEntity toItemEntity(ItemDto itemDto);

	@Mapping(target = "historyId", expression = "java(generateCodeIfNotExists(orderHistoryEntity.getHistoryId()))")
	OrderHistoryEntity toOrderHistoryEntity(OrderHistoryDto orderHistoryDto);

	OrderStatusEntity toOrderStatusEntity(OrderStatusDto orderStatusDto);

	@Mapping(target = "supplierRetailerId", expression = "java(generateCodeIfNotExists(supplierRetailerEntity.getSupplierRetailerId()))")
	SupplierRetailerEntity toSupplierRetailerEntity(SupplierRetailerDto supplierRetailerDto);

	@Mapping(target = "ratingId", expression = "java(generateCodeIfNotExists(ratingEntity.getRatingId()))")
	RatingEntity toRatingEntity(RatingDto ratingDto);

	OrderDto toOrderDto(OrderEntity orderEntity);

	ItemDto toItemDto(ItemEntity itemEntity);

	OrderHistoryDto toOrderHistoryDto(OrderHistoryEntity orderHistoryEntity);

	OrderStatusDto toOrderStatusDto(OrderStatusEntity orderStatusEntity);

	SupplierRetailerDto toSupplierRetailerDto(SupplierRetailerEntity SupplierRetailerEntity);

	RatingDto toRatingDto(RatingEntity ratingEntity);
	
	default Long generateCodeIfNotExists(Long existingCode) {
		if (existingCode == null || existingCode == 0L) {
			long min = 100_000L;
			long max = 999_999L;
			return min + (long) (Math.random() * (max - min + 1));
		}
		return existingCode;
	}

	default UUID generateOrderGuidIfNotExists(UUID existingOrderGuid) {
		if (existingOrderGuid == null) {
			return UUID.randomUUID();
		}
		return existingOrderGuid;
	}
	
	default public Date convertUtilDateToSqlDate(java.util.Date utilDate) {
        if (utilDate != null) {
            return new Date(utilDate.getTime());
        }
        return null;
    }
}
