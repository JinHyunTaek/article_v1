package com.example.article.api.error.errorresponse;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberErrorResponse {

    private Optional<List<String>> memberErrorFields;
    private Optional<String> memberErrorField;
    private String memberErrorCode;
    private String errorMessage;
}
