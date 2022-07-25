package com.example.article.web.controller;

import com.example.article.web.service.HomeWebService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final HomeWebService homeService;

    @GetMapping("/")
    public String home(@SessionAttribute(name = "memberId", required = false) Long memberId,
//                       @PageableDefault(sort = "id",direction = Sort.Direction.DESC, size = 10) Pageable pageable,
                       Model model) {
        homeService.setHomeCondition(model);

        if (memberId == null) {
            return "home";
        }

        model.addAttribute("member", homeService.findById(memberId));
        return "loginHome";
    }

}
