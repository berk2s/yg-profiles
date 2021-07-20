package com.yataygecisle.preference.profiles.web.models;

import com.yataygecisle.commons.annotations.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BasketDto implements Serializable {

    static final long serialVersionUID = 41849813095130712L;

    @UUID
    private String basketId;

    @UUID
    private String ownerId;

    private String basketName;

    private List<BasketItemDto> basketItems = new ArrayList<>();

    private Timestamp createdAt;

    private Timestamp lastModifiedAt;

}
