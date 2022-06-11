package com.example.article.api.error.errorresponse;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SimpleErrorResponse {

    private String url;
    private String message;
}
