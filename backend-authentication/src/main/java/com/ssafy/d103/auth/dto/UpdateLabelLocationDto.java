package com.ssafy.d103.auth.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdateLabelLocationDto {
    private String superLabelId;
    private String subLabelId;
}
