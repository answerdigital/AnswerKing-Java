package com.answerdigital.benhession.academy.answerkingweek2.mappers;

import com.answerdigital.benhession.academy.answerkingweek2.model.Order;
import com.answerdigital.benhession.academy.answerkingweek2.request.OrderRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "orderStatus", constant = "IN_PROGRESS")
    Order addRequestToOrder(OrderRequest orderRequest);

    Order updateOrderRequest(@MappingTarget Order order, OrderRequest orderRequest);
}
