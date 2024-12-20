package com.example.servingwebcontent.controller.medical;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class MedicalController {

    @GetMapping("/medical/list")
    public String medicalList(
        Model model
    ) {
        model.addAttribute("medicalAdminTab", "active");
        return "medical/admin/topicList";
    }

    @GetMapping("/medical/{topicId}")
    public String topic(
        @PathVariable Long topicId,
        Model model
    ) {
        model.addAttribute("medicalAdminTab", "active");
        return "/medical/admin/topic";
    }
}

