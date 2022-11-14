package com.answerdigital.answerking.request;

import javax.validation.constraints.NotBlank;

public record OrderRequest(@NotBlank String address) {
}
