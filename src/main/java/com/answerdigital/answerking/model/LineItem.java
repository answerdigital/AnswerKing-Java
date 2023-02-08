package com.answerdigital.answerking.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Entity
@Table(name = "order_product")
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@AllArgsConstructor
public class LineItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "quantity")
    private Integer quantity;

    public LineItem(final Order order, final Product product, final int quantity) {
        this.order = order;
        this.product = product;
        this.quantity = quantity;
    }

    /**
     * This method checks if the LineItem is the exact same as the selected object
     * (normally Product {@link com.answerdigital.answerking.model.LineItem} object).
     * @return A boolean verifying whether the objects are the same or not.
     */
    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final LineItem lineItem = (LineItem) o;
        return (
            Objects.equals(order, lineItem.order)
            && Objects.equals(product, lineItem.product)
            && Objects.equals(quantity, lineItem.quantity));
    }

    /**
     * This method returns a hash of the LineItem.
     * @return A Hash value for an Order ID, Product ID and Product Quantity.
     */
    @Override
    public int hashCode() {
        return Objects.hash(order, product, quantity);
    }

    @Override
    public String toString() {
        return "LineItem{" +
                "id=" + id +
                ", orderId=" + order.getId() +
                ", productId=" + product.getId() +
                ", quantity=" + quantity +
                '}';
    }
}
