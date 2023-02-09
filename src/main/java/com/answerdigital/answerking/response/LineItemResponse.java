package com.answerdigital.answerking.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * The class LineItemResponse represents the LineItem {@link com.answerdigital.answerking.model.LineItem}
 * to be returned, after the end-user has sent an API endpoint request.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LineItemResponse {

    private ProductResponse product;

    private Integer quantity;

    /**
     * This method calculates the subtotal price of the Product
     * {@link com.answerdigital.answerking.model.Product}.
     * @return The subtotal price.
     */
    public BigDecimal getSubTotal() {
        return new BigDecimal(quantity).multiply(product.getPrice()).setScale(2, RoundingMode.DOWN);
    }
}
