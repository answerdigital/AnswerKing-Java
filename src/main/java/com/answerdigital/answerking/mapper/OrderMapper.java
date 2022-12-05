package com.answerdigital.answerking.mapper;

import com.answerdigital.answerking.model.Order;
import com.answerdigital.answerking.response.OrderResponse;
import org.mapstruct.Mapper;
import java.util.Collections;

@Mapper(componentModel = "spring", imports = Collections.class)
public interface OrderMapper {

    OrderResponse orderToOrderResponse(Order order);
}
