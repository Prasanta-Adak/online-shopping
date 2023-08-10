package com.codebyprasanta.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.codebyprasanta.dto.OrderLineItemDto;
import com.codebyprasanta.dto.OrderRequest;
import com.codebyprasanta.entity.Order;
import com.codebyprasanta.entity.OrderLineItem;
import com.codebyprasanta.repository.OrderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {
	
	private final OrderRepository orderRepository;

	public void placeOrder(OrderRequest orderRequest) {
		Order order=new Order();
		order.setOrderNumber(UUID.randomUUID().toString());
		
		List<OrderLineItem> orderLineItems = orderRequest.getOrderLineItemsDtoList()
		           .stream()
		           .map(this::mapToDto)
		           .toList();
		
		order.setOrderLineItemList(orderLineItems);
		
		orderRepository.save(order);
	}
	
	
	private OrderLineItem mapToDto(OrderLineItemDto orderLineItemDto) {
		OrderLineItem orderLineItem=new OrderLineItem();
		orderLineItem.setPrice(orderLineItemDto.getPrice());
		orderLineItem.setQuantity(orderLineItemDto.getQuantity());
		orderLineItem.setSkuCode(orderLineItemDto.getSkuCode());
		
		return orderLineItem;
	}
}
