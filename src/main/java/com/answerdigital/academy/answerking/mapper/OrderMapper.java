package com.answerdigital.academy.answerking.mapper;

import com.answerdigital.academy.answerking.model.Order;
import com.answerdigital.academy.answerking.request.OrderRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import java.util.Collections;

@Mapper(componentModel = "spring", imports = Collections.class)
public interface OrderMapper {

    @Mapping(target = "orderStatus", constant = "IN_PROGRESS")
    @Mapping(target = "orderItems", expression = "java(Collections.EMPTY_SET)")
    Order addRequestToOrder(OrderRequest orderRequest);

    Order updateOrderRequest(@MappingTarget Order order, OrderRequest orderRequest);
}
