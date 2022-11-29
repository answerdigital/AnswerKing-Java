package com.answerdigital.answerking.response;

import com.answerdigital.answerking.model.LineItem;
import com.answerdigital.answerking.model.OrderStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.HashSet;
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

    private Set<LineItem> lineItems = new HashSet<>();

    @JsonInclude
    public BigDecimal getOrderTotal() {
        return lineItems.stream()
            .map(lineItem -> lineItem.getProduct().getPrice()
                .multiply(BigDecimal.valueOf(lineItem.getQuantity())))
            .reduce(BigDecimal::add)
            .orElse(BigDecimal.ZERO)
            .setScale(2, RoundingMode.DOWN);
    }

    @Override
    public String toString() {
        return "OrderResponse{" +
            "id=" + id +
            ", createdOn=" + createdOn +
            ", lastUpdated=" + lastUpdated +
            ", orderStatus=" + orderStatus +
            ", lineItems=" + lineItems +
            ", orderTotal=" + getOrderTotal().toString() +
            '}';
    }
}
