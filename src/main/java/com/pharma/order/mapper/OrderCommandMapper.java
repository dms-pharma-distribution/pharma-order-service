package com.pharma.order.mapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pharma.order.dto.ItemDto;
import com.pharma.order.dto.OrderDto;
import com.pharma.order.dto.OrderCreateListDto;
import com.pharma.order.dto.OrderStatusDto;
import com.pharma.order.dto.SupplierRetailerDto;
import com.pharma.order.entity.ItemEntity;
import com.pharma.order.entity.OrderEntity;
import com.pharma.order.entity.OrderHistoryEntity;
import com.pharma.order.entity.OrderStatusEntity;
import com.pharma.order.entity.SupplierRetailerEntity;

@Component
public class OrderCommandMapper {

	OrderMapper orderMapper;

	public OrderCommandMapper(OrderMapper orderMapper) {
		this.orderMapper = orderMapper;
	}

	public List<OrderEntity> mapToOrderEntities(OrderCreateListDto crderDtoCollection) {
		return crderDtoCollection.getOrderDtos().stream().map(orderDto -> mapToOrderEntity(orderDto, null))
				.collect(Collectors.toList());
	}

	public OrderEntity mapToUpdateOrderEntity(OrderDto orderDto, OrderEntity previousOrderEntity) {
		return mapToOrderEntity(orderDto, previousOrderEntity);
	}

	public OrderEntity mapToOrderEntity(OrderDto orderDto, OrderEntity previousOrderEntity) {
		var orderEntity = orderMapper.toOrderEntity(orderDto);
		var itemEntities = orderDto.getItemDtos().stream().map(itemDto -> mapToItemEntity(itemDto, orderEntity))
				.collect(Collectors.toList());
		orderEntity.setItemEntities(itemEntities);
		orderEntity.setOrderStatusEntity(mapToOrderStatusEntity(orderDto.getOrderStatusDto()));
		orderEntity
				.setSupplierRetailerEntity(mapToSupplierRetailerEntity(orderDto.getSupplierRetailerDto(), orderEntity));
		List<OrderHistoryEntity> orderHistoryList = new ArrayList<>();
		orderHistoryList.add(mapToOrderHistoryEntity(orderEntity, previousOrderEntity));
		orderEntity.setOrderHistoryEntity(orderHistoryList);
		return calculateTotalValues(orderEntity);
	}

	public ItemEntity mapToItemEntity(ItemDto itemDto, OrderEntity orderEntity) {
		var itemEntity = orderMapper.toItemEntity(itemDto);
		itemEntity.setOrderEntity(orderEntity);
		itemEntity.setSubTotalValue(itemEntity.getMrp() * itemEntity.getQuantity());
		return itemEntity;
	}

	public OrderStatusEntity mapToOrderStatusEntity(OrderStatusDto drderStatusDto) {
		return orderMapper.toOrderStatusEntity(drderStatusDto);
	}

	public SupplierRetailerEntity mapToSupplierRetailerEntity(SupplierRetailerDto supplierRetailerDto,
			OrderEntity orderEntity) {
		var supplierRetailerEntity = orderMapper.toSupplierRetailerEntity(supplierRetailerDto);
		supplierRetailerEntity.setOrderEntity(orderEntity);
		return supplierRetailerEntity;
	}

	public OrderHistoryEntity mapToOrderHistoryEntity(OrderEntity newOrderEntity, OrderEntity previousOrderEntity) {
		var orderHistoryEntity = new OrderHistoryEntity();
		orderHistoryEntity.setHistoryId(orderMapper.generateCodeIfNotExists(orderHistoryEntity.getHistoryId()));
		orderHistoryEntity.setOrderEntity(newOrderEntity);
		orderHistoryEntity.setEventType(newOrderEntity.getOrderStatusEntity().getStatusCode());
		orderHistoryEntity.setEventSource(newOrderEntity.getSourceType());
		orderHistoryEntity.setEventTimestamp(LocalDateTime.now());
		orderHistoryEntity.setNewState(convertJson(orderMapper.toOrderDto(newOrderEntity)));
		orderHistoryEntity.setPreviousState(convertJson(orderMapper.toOrderDto(previousOrderEntity)));
		orderHistoryEntity.setUpdatedBy(newOrderEntity.getCreatedBy());
		orderHistoryEntity.setEventDescription(newOrderEntity.getOrderStatusEntity().getDescription());
		return orderHistoryEntity;
	}

	public String convertJson(OrderDto orderDto) {
		String orderString = null;
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			orderString = objectMapper.writeValueAsString(orderDto);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return orderString;
	}

	public OrderEntity calculateTotalValues(OrderEntity orderEntity) {
		var totalOrderValue = orderEntity.getItemEntities().stream().mapToDouble(ItemEntity::getSubTotalValue).sum();
		var totalGst = totalOrderValue / 100 * 18;
		orderEntity.setTotalProducts(orderEntity.getItemEntities().size());
		orderEntity.setTotalQuantity(orderEntity.getItemEntities().stream().mapToDouble(ItemEntity::getQuantity).sum());
		orderEntity.setTotalGstValue(totalGst);
		orderEntity.setTotalOrderValue(totalOrderValue + totalGst);
		return orderEntity;
	}
}
