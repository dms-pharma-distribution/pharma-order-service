package com.pharma.order.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pharma.order.entity.OrderEntity;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, UUID> {
	
	public Optional<OrderEntity> findByOrderCode(Long orderCode);
	
	public Optional<OrderEntity> findByOrderGuidOrOrderCode(UUID orderGuid, Long orderCode);
}
