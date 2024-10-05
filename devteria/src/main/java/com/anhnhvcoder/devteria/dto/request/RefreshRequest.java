package com.anhnhvcoder.devteria.dto.request;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RefreshRequest {

    private String token;
}
