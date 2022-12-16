package com.answerdigital.answerking.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record OrderRequest(
        @JsonProperty("lineItems")
        List<LineItemRequest> lineItemRequests
) { }
