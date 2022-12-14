package com.answerdigital.answerking.builder;

import com.answerdigital.answerking.request.LineItemRequest;
import com.answerdigital.answerking.request.OrderRequest;

import java.util.ArrayList;
import java.util.List;

public class OrderRequestTestBuilder {
    private List<LineItemRequest> lineItemRequests;

    public OrderRequestTestBuilder withDefaultValues() {
        this.lineItemRequests = new ArrayList<>();
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
