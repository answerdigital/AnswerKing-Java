package com.answerdigital.answerking.mapper;

import com.answerdigital.answerking.model.Order;
import com.answerdigital.answerking.request.OrderRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import java.util.Collections;

@Mapper(componentModel = "spring", imports = Collections.class)
public interface OrderMapper {

    @Mapping(target = "orderStatus", constant = "CREATED")
    @Mapping(target = "lineItems", expression = "java(Collections.EMPTY_SET)")
    Order addRequestToOrder(OrderRequest orderRequest);

    Order updateOrderRequest(@MappingTarget Order order, OrderRequest orderRequest);
}
