package com.example.servingwebcontent.controller.medical;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class MedicalResultController {

    @GetMapping("topic/result/{userId}")
    public String getTopicList(
        @PathVariable Long userId,
        Model model
    ) {
        model.addAttribute("userId", userId);
        model.addAttribute("usersTab", "active");
        return "medical/result/topicList";
    }

    @GetMapping("topic/result/{userId}/{topicResultId}")
    public String getTopicResult(
        @PathVariable Long userId,
        @PathVariable Long topicResultId,
        Model model
    ) {
        model.addAttribute("userId", userId);
        model.addAttribute("topicResultId", topicResultId);
        model.addAttribute("usersTab", "active");
        return "medical/result/topic";
    }

}
