package com.answerdigital.answerking.builder.order;

import com.answerdigital.answerking.request.LineItemRequest;
import com.answerdigital.answerking.request.OrderRequest;

import java.util.List;

public class OrderRequestTestBuilder {
    private List<LineItemRequest> lineItemRequests;

    public OrderRequestTestBuilder withDefaultValues() {
        this.lineItemRequests = List.of(
            new LineItemRequest(1L, 1)
        );
        return this;
    }

    public OrderRequestTestBuilder withLineItemRequests(final List<LineItemRequest> lineItemRequests) {
        this.lineItemRequests = lineItemRequests;
        return this;
    }

    public OrderRequest build() {
        return new OrderRequest(lineItemRequests);
    }
}
