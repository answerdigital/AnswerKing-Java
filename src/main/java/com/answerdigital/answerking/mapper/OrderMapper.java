package com.answerdigital.answerking.mapper;

import com.answerdigital.answerking.model.Order;
import com.answerdigital.answerking.model.Product;
import com.answerdigital.answerking.response.OrderResponse;
import com.answerdigital.answerking.response.ProductResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Collections;

@Mapper(componentModel = "spring", imports = Collections.class)
public interface OrderMapper {

    @Mapping(source = "lineItems", target = "lineItems")
    OrderResponse orderToOrderResponse(Order order);

    @Mapping(source = "category.id", target = "category")
    ProductResponse convertProductEntityToProductResponse(Product product);
}
