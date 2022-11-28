package com.answerdigital.answerking.builder;

import com.answerdigital.answerking.model.LineItem;
import com.answerdigital.answerking.model.Order;
import com.answerdigital.answerking.model.OrderStatus;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class OrderTestBuilder {
    private Long id = 1L;
    private LocalDateTime createdOn = LocalDateTime.now();
    private LocalDateTime lastUpdated = LocalDateTime.now();
    private OrderStatus orderStatus = OrderStatus.CREATED;
    private Set<LineItem> lineItems = new HashSet<>();

    public OrderTestBuilder withId(final Long id) {
        this.id = id;
        return this;
    }

    public OrderTestBuilder withCreatedOn(final LocalDateTime createdOn) {
        this.createdOn = createdOn;
        return this;
    }

    public OrderTestBuilder withLastUpdated(final LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
        return this;
    }

    public OrderTestBuilder withOrderStatus(final OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
        return this;
    }

    public OrderTestBuilder withLineItems(final Set<LineItem> lineItems) {
        this.lineItems = lineItems;
        return this;
    }

    public OrderTestBuilder withLineItem(final LineItem lineItem) {
        this.lineItems.add(lineItem);
        return this;
    }

    public Order build() {
        return new Order(id, createdOn, lastUpdated, orderStatus, lineItems);
    }
}
