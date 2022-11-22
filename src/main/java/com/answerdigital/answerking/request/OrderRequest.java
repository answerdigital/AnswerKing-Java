package com.answerdigital.answerking.request;

import javax.validation.Valid;
import java.util.List;

public record OrderRequest(
        @Valid
        List<LineItemRequest> lineItems
) { }
