package com.pharma.order.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pharma.order.entity.OrderEntity;
import com.pharma.order.entity.OrderHistoryEntity;

@Repository
public interface OrderHistoryRepository extends JpaRepository<OrderHistoryEntity, UUID> {
	
	public List<OrderHistoryEntity> findByOrderEntity(OrderEntity orderEntity);
}
