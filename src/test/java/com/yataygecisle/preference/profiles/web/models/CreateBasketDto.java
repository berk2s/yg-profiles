package com.yataygecisle.preference.profiles.web.models;

import com.yataygecisle.commons.annotations.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateBasketDto {

    @NotNull(message = "Basket name must not be empty")
    private String basketName;

    @UUID(message = "Owner ID must be UUID")
    @NotNull(message = "Owner ID must not be empty")
    private String ownerId;

    @NotNull(message = "Basket items must not be null")
    private Set<AddBasketItemDto> basketItems = new HashSet<>();

}
