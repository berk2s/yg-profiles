package com.yataygecisle.preference.profiles.web.models;

import com.yataygecisle.commons.annotations.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddBasketItemDto {

    @UUID(message = "Basket Item ID must be UUID")
    @NotNull(message = "Basket Item ID must not be empty")
    private String basketItemId;

}
