package com.answerdigital.answerking.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * The class OrderRequest represents the Order {@link com.answerdigital.answerking.model.Order}
 * received from API endpoint requests.
 */
public record OrderRequest(
        @JsonProperty("lineItems")
        List<LineItemRequest> lineItemRequests
) { }
