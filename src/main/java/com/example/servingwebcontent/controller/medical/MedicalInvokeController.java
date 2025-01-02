package com.example.servingwebcontent.controller.medical;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class MedicalInvokeController {

    @GetMapping("/medical/invoke")
    public String topicList(
        Model model
    ) {
        model.addAttribute("invokeTopicTab", "active");
        return "medical/invoke/topicList";
    }

    @GetMapping("/medical/invoke/{topicResultId}")
    public String getMedicalInvoke(
        @PathVariable Long topicResultId,
        Model model
    ) {
        model.addAttribute("topicResultId", topicResultId);
        model.addAttribute("invokeTopicTab", "active");
        return "medical/invoke/topic";
    }
}
