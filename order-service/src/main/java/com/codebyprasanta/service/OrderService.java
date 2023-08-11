package com.codebyprasanta.service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;

import com.codebyprasanta.dto.InventoryResponse;
import com.codebyprasanta.dto.OrderLineItemDto;
import com.codebyprasanta.dto.OrderRequest;
import com.codebyprasanta.entity.Order;
import com.codebyprasanta.entity.OrderLineItem;
import com.codebyprasanta.repository.OrderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {
	
	private final OrderRepository orderRepository;
	private final WebClient webClient;

	public void placeOrder(OrderRequest orderRequest) {
		Order order=new Order();
		order.setOrderNumber(UUID.randomUUID().toString());
		
		List<OrderLineItem> orderLineItems = orderRequest.getOrderLineItemsDtoList()
		           .stream()
		           .map(this::mapToDto)
		           .toList();
		
		order.setOrderLineItemList(orderLineItems);
		
		List<String> skuCodes = order.getOrderLineItemList().stream()
		            .map(orderLineItem -> orderLineItem.getSkuCode())
		            .toList();
		//call inventory service and check product is in stock
		InventoryResponse[] inventoryResponseArray = webClient.get()
		         .uri("http://localhost:8082/api/inventory",
		        		 UriBuilder -> UriBuilder.queryParam("skuCode", skuCodes).build())
		         .retrieve()
		         .bodyToMono(InventoryResponse[].class)
		         .block();
		
		boolean allProductsInStock = Arrays.stream(inventoryResponseArray)
				.allMatch(inventoryResponse -> inventoryResponse.isInStock());
		
		if(allProductsInStock) {
		orderRepository.save(order);
		}else {
			throw new IllegalArgumentException("Product is not in stock,please try again later");
		}
		
	}
	
	
	private OrderLineItem mapToDto(OrderLineItemDto orderLineItemDto) {
		OrderLineItem orderLineItem=new OrderLineItem();
		orderLineItem.setPrice(orderLineItemDto.getPrice());
		orderLineItem.setQuantity(orderLineItemDto.getQuantity());
		orderLineItem.setSkuCode(orderLineItemDto.getSkuCode());
		
		return orderLineItem;
	}
}
