package com.pharma.order.controller;

import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pharma.order.dto.OrderCreateListDto;
import com.pharma.order.dto.OrderDto;
import com.pharma.order.dto.RatingDto;
import com.pharma.order.service.OrderService;

@RestController
@RequestMapping("/svc/order/v1")
public class OrderController {

	OrderService orderService;

	public OrderController(OrderService orderService) {
		this.orderService = orderService;
	}

	@PostMapping("/createOrders")
	public void createOrders(@RequestBody OrderCreateListDto orderCreateListDto) {
		orderService.createOrders(orderCreateListDto);
	}

	@GetMapping("/findByOrderCode")
	public OrderDto findOrderByOrderCode(@RequestParam("orderCode") long orderCode) {
		return orderService.findOrderByOrderCode(orderCode);
	}

	@GetMapping("/findByOrderGuid")
	public OrderDto findOrderByOrderGuid(@RequestParam("orderGuid") UUID orderGuid) {
		return orderService.findOrderByOrderGuid(orderGuid);
	}

	@PostMapping("/createRatinForOrder")
	public void createRatinForOrder(Long orderCode, RatingDto ratingDto) {
		orderService.createRatinForOrder(orderCode, ratingDto);
	}

	@PutMapping("/updateorder")
	public void updateOrder(@RequestBody OrderDto ordersDto, @RequestParam("orderCode") Long orderCode) {
		orderService.updateOrder(ordersDto, orderCode);
	}
}
