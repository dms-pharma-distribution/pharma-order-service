package com.pharma.order.controller;

import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pharma.order.dto.OrderQueryDto;
import com.pharma.order.dto.OrderCreateListDto;
import com.pharma.order.service.OrderService;

@RestController
@RequestMapping("svc/order/v1")
public class OrderController {

	OrderService orderService;

	public OrderController(OrderService orderService) {
		this.orderService = orderService;
	}

	@PostMapping("/createOrders")
	public void createOrders(@RequestBody OrderCreateListDto orderCreateListDto) {
		orderService.createOrders(orderCreateListDto);
	}

	/*
	 * @PutMapping("/updateOrder") public void updateOrder(@RequestBody OrderDto
	 * orderDto, @RequestParam("orderCode") long orderCode) {
	 * orderService.updateOrder(orderDto, orderCode); }
	 * 
	 * @DeleteMapping("/deleteOrder") public void
	 * deleteOrder(@RequestParam("orderCode") long orderCode) {
	 * orderService.deleteOrder(orderCode); }
	 */

	@GetMapping("/findByOrderCode")
	public OrderQueryDto findOrderByOrderCode(@RequestParam("orderCode") long orderCode) {
		return orderService.findOrderByOrderCode(orderCode);
	}

	@GetMapping("/findByOrderGuid")
	public OrderQueryDto findOrderByOrderGuid(@RequestParam("orderGuid") UUID orderGuid) {
		return orderService.findOrderByOrderGuid(orderGuid);
	}

}
