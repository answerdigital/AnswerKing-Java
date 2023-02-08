package com.answerdigital.answerking.response;

import com.answerdigital.answerking.model.OrderStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
    private Long id;

    private LocalDateTime createdOn;

    private LocalDateTime lastUpdated;

    private OrderStatus orderStatus;

    private Set<LineItemResponse> lineItems;

    /**
     * This method calculates the total price of the Order
     * {@link com.answerdigital.answerking.model.Order}.
     * @return The total price of the Order {@link com.answerdigital.answerking.model.Order}.
     */
    @JsonInclude
    public BigDecimal getOrderTotal() {
        return lineItems.stream()
            .map(lineItem -> lineItem.getProduct().getPrice()
                .multiply(BigDecimal.valueOf(lineItem.getQuantity())))
            .reduce(BigDecimal::add)
            .orElse(BigDecimal.ZERO)
            .setScale(2, RoundingMode.DOWN);
    }
}
