package com.answerdigital.academy.answerking.request;

import javax.validation.constraints.NotBlank;

public record OrderRequest(@NotBlank String address) {
}
