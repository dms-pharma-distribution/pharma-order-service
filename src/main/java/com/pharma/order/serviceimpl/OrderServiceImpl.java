package com.pharma.order.serviceimpl;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.GetQueueUrlResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pharma.order.dto.OrderDto;
import com.pharma.order.dto.OrderQueryDto;
import com.pharma.order.dto.OrderCreateListDto;
import com.pharma.order.entity.OrderEntity;
import com.pharma.order.mapper.OrderCommandMapper;
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

	OrderCommandMapper orderCommandMapper;

	OrderQueryMapper orderQueryMapper;

	RatingRepository ratingRepository;

	@Autowired
	ObjectMapper objectMapper;

	@Value("${app.config.message.queue.topic}")
	String messageQueueTopic;

	AmazonSQS amazonSQSClient;

	@Autowired
	public OrderServiceImpl(OrderRepository orderRepository, OrderHistoryRepository orderHistoryRepository,
			OrderCommandMapper orderCommandMapper, OrderQueryMapper orderQueryMapper, RatingRepository ratingRepository,
			AmazonSQS amazonSQSClient) {
		this.orderRepository = orderRepository;
		this.orderHistoryRepository = orderHistoryRepository;
		this.orderCommandMapper = orderCommandMapper;
		this.orderQueryMapper = orderQueryMapper;
		this.ratingRepository = ratingRepository;
		this.amazonSQSClient = amazonSQSClient;
	}

	@Override
	public void createOrders(OrderCreateListDto orderCreateListDto) {
		orderCreateListDto.getOrderDtos().forEach(order -> {
			orderSQSProducer(order);
		});
	}

	@Override
	public OrderQueryDto findOrderByOrderCode(long orderCode) {
		OrderEntity existsOrderEntity = orderRepository.findByOrderCode(orderCode)
				.orElseThrow(() -> new RuntimeException("Order not found"));
		return orderQueryMapper.mapToQueryDto(existsOrderEntity,
				orderHistoryRepository.findByOrderEntity(existsOrderEntity),
				ratingRepository.findByOrderGuid(existsOrderEntity.getOrderGuid()));
	}

	@Override
	public OrderQueryDto findOrderByOrderGuid(UUID orderGuid) {
		OrderEntity existsOrderEntity = orderRepository.findById(orderGuid)
				.orElseThrow(() -> new RuntimeException("Order not found"));
		return orderQueryMapper.mapToQueryDto(existsOrderEntity,
				orderHistoryRepository.findByOrderEntity(existsOrderEntity),
				ratingRepository.findByOrderGuid(orderGuid));
	}

	public void orderSQSProducer(OrderDto orderEntity) {
		try {
			String orderJsonMessage = objectMapper.writeValueAsString(orderEntity);
			GetQueueUrlResult queueUrl = amazonSQSClient.getQueueUrl(messageQueueTopic);
			amazonSQSClient.sendMessage(queueUrl.getQueueUrl(), orderJsonMessage);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}
}
