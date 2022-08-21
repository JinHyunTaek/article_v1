package com.example.article.api.controller;

import com.example.article.api.ApiResult;
import com.example.article.api.dto.reply.CreateReplyDto;
import com.example.article.api.dto.reply.CreateReplyDto.CreateReplyRequest;
import com.example.article.api.dto.reply.CreateReplyDto.CreateReplyResponse;
import com.example.article.api.service.ReplyApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reply")
public class ReplyApiController {

    private final ReplyApiService replyService;

    @GetMapping("/save")
    public ResponseEntity<ApiResult<CreateReplyResponse>> save(
            @Valid @RequestBody CreateReplyRequest request
    ){
        ApiResult<CreateReplyResponse> response = replyService.save(request);

        return ResponseEntity
                .ok()
                .body(response);
    }

}
