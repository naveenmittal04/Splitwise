package com.naveenmittal.splitwise.dtos;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MyTotalResponseDto {
    private ResponseStatus status;
    private Long total;
}
