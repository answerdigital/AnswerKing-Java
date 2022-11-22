package com.answerdigital.answerking.request;

import java.util.List;

public record OrderRequest(
        List<LineItemRequest> lineItems
) { }
