package com.vdcrx.rest.api.v1.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class VetCollDto {

    @JsonProperty("size")
    private int size;

    private List<VetDto> veterinarians;
}
