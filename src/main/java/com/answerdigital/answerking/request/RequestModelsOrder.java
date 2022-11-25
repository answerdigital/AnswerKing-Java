package com.answerdigital.answerking.request;

import javax.validation.constraints.NotBlank;

public record RequestModelsOrder(@NotBlank String address) {
}
