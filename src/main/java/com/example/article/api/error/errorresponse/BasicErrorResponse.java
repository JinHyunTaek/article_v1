package com.example.article.api.error.errorresponse;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BasicErrorResponse {

    private List<String> memberErrorFields;
    private String memberErrorCode;
    private String errorMessage;

    public void addErrorField(String errorField){
        memberErrorFields.add(errorField);
    }
}
