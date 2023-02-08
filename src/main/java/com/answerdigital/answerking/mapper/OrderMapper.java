package com.answerdigital.answerking.mapper;

import com.answerdigital.answerking.model.Order;
import com.answerdigital.answerking.model.Product;
import com.answerdigital.answerking.response.OrderResponse;
import com.answerdigital.answerking.response.ProductResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Collections;

/**
 * The class OrderMapper is the mapping layer for Orders {@link com.answerdigital.answerking.model.Order}.
 * It handles interactions with OrderRequest {@link com.answerdigital.answerking.request.OrderRequest}
 * and OrderResponse {@link com.answerdigital.answerking.response.OrderResponse}.
 */
@Mapper(componentModel = "spring", imports = Collections.class)
public interface OrderMapper {

    @Mapping(source = "lineItems", target = "lineItems")
    OrderResponse orderToOrderResponse(Order order);

    @Mapping(source = "category.id", target = "category")
    ProductResponse convertProductEntityToProductResponse(Product product);
}
