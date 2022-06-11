package com.example.article.api.error.errorresponse;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BasicErrorResponse {

    private String url;
    private String message;
}
