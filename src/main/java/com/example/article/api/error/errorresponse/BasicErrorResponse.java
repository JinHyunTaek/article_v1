package com.example.article.api.error.errorresponse;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BasicErrorResponse {

    private List<String> errorFields;

    private String errorField;

    private String errorCode;

    private String errorMessage;

    public void addErrorField(String errorField){
        errorFields.add(errorField);
    }
}
