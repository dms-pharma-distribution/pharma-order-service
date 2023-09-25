package com.pharma.order.serviceimpl;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.GetQueueUrlResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pharma.order.dto.OrderCreateListDto;
import com.pharma.order.dto.OrderDto;
import com.pharma.order.dto.RatingDto;
import com.pharma.order.entity.OrderEntity;
import com.pharma.order.entity.RatingEntity;
import com.pharma.order.mapper.OrderMapper;
import com.pharma.order.mapper.OrderQueryMapper;
import com.pharma.order.repository.OrderHistoryRepository;
import com.pharma.order.repository.OrderRepository;
import com.pharma.order.repository.RatingRepository;
import com.pharma.order.service.OrderService;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

	OrderRepository orderRepository;

	OrderHistoryRepository orderHistoryRepository;

	OrderMapper orderMapper;

	OrderQueryMapper orderQueryMapper;

	RatingRepository ratingRepository;

	@Autowired
	ObjectMapper objectMapper;

	@Value("${app.config.message.queue.topic}")
	String messageQueueTopic;

	AmazonSQS amazonSQSClient;

	@Autowired
	public OrderServiceImpl(OrderRepository orderRepository, OrderHistoryRepository orderHistoryRepository,
			OrderMapper orderMapper, OrderQueryMapper orderQueryMapper, RatingRepository ratingRepository,
			ObjectMapper objectMapper, String messageQueueTopic, AmazonSQS amazonSQSClient) {
		super();
		this.orderRepository = orderRepository;
		this.orderHistoryRepository = orderHistoryRepository;
		this.orderMapper = orderMapper;
		this.orderQueryMapper = orderQueryMapper;
		this.ratingRepository = ratingRepository;
		this.objectMapper = objectMapper;
		this.messageQueueTopic = messageQueueTopic;
		this.amazonSQSClient = amazonSQSClient;
	}

	@Override
	public void createOrders(OrderCreateListDto orderCreateListDto) {
		orderCreateListDto.getOrderDtos().forEach(order -> {
			validateOrders(order);
		});
	}

	@Override
	public OrderDto findOrderByOrderCode(long orderCode) {
		OrderEntity existsOrderEntity = orderRepository.findByOrderCode(orderCode)
				.orElseThrow(() -> new RuntimeException("Order not found"));
		return orderQueryMapper.mapToOrderDto(existsOrderEntity);
	}

	@Override
	public OrderDto findOrderByOrderGuid(UUID orderGuid) {
		OrderEntity existsOrderEntity = orderRepository.findById(orderGuid)
				.orElseThrow(() -> new RuntimeException("Order not found"));
		return orderQueryMapper.mapToOrderDto(existsOrderEntity);
	}

	public void orderSQSProducer(OrderDto orderDto) {
		try {
			String orderJsonMessage = objectMapper.writeValueAsString(orderDto);
			GetQueueUrlResult queueUrl = amazonSQSClient.getQueueUrl(messageQueueTopic);
			amazonSQSClient.sendMessage(queueUrl.getQueueUrl(), orderJsonMessage);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void createRatinForOrder(Long orderCode, RatingDto ratingDto) {
		Optional<OrderEntity> orderEntityOptional = orderRepository.findByOrderCode(orderCode);
		RatingEntity ratingEntity = orderMapper.toRatingEntity(ratingDto);
		ratingEntity.setOrderGuid(orderEntityOptional.get().getOrderGuid());
		ratingRepository.save(ratingEntity);
	}

	public OrderDto validateOrders(OrderDto orderDto) {
		Optional<OrderEntity> orderEntityOptional = orderRepository.findByOrderGuidOrOrderCode(orderDto.getOrderGuid(),
				orderDto.getOrderCode());
		if (!orderEntityOptional.isPresent()) {
			orderDto.setOrderGuid(UUID.randomUUID());
			orderDto.setOrderCode(orderMapper.generateCodeIfNotExists());
			orderSQSProducer(orderDto);
		} else {
			throw new RuntimeException("Order is already found");
		}
		return orderDto;
	}

	@Override
	public void updateOrder(OrderDto ordersDto, Long orderCode) {
		Optional<OrderEntity> orderEntityOptional = orderRepository.findByOrderCode(orderCode);
		if (orderEntityOptional.isPresent()) {
			orderSQSProducer(ordersDto);
		} else {
			throw new RuntimeException("Order not found");
		}
	}
}
