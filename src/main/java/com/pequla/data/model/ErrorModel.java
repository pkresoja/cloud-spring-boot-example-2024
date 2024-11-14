package com.pequla.data.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ErrorModel {
    private String name;
    private String message;
    private String path;
    private Long timestamp;
}
