package com.answerdigital.answerking.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    private LocalDateTime createdOn;

    @UpdateTimestamp
    private LocalDateTime lastUpdated;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @OneToMany(
            mappedBy = "order",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER
    )
    private Set<LineItem> lineItems = new HashSet<>();

    public Order() {
        this.orderStatus = OrderStatus.CREATED;
    }

    /**
     * This method adds a LineItem {@link com.answerdigital.answerking.model.LineItem}
     * to the Order.
     */
    public void addLineItem(final LineItem lineItem) {
        lineItems.add(lineItem);
    }

    /**
     * This method removes LineItems {@link com.answerdigital.answerking.model.LineItem}
     * from the Order.
     */
    public void clearLineItems() {
        lineItems.clear();
    }
}
