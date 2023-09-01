package com.example.servingwebcontent.controller.medical;

import com.example.servingwebcontent.domain.User;
import com.example.servingwebcontent.domain.medical.MedicalTopic;
import com.example.servingwebcontent.domain.medical.result.MedicalTaskResult;
import com.example.servingwebcontent.domain.medical.result.MedicalTopicResult;
import com.example.servingwebcontent.service.medical.MedicalTopicInvokeService;
import com.example.servingwebcontent.service.medical.MedicalTopicResultService;
import com.example.servingwebcontent.service.medical.MedicalTopicService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/result/topic")
@PreAuthorize("hasAuthority('ADMIN')")
public class MedicalResultController {

    private final MedicalTopicService topicService;
    private final MedicalTopicResultService topicResultService;
    private final MedicalTopicInvokeService topicInvokeService;

    public MedicalResultController(MedicalTopicService topicService, MedicalTopicResultService topicResultService, MedicalTopicInvokeService topicInvokeService) {
        this.topicService = topicService;
        this.topicResultService = topicResultService;
        this.topicInvokeService = topicInvokeService;
    }

    @GetMapping("/{user}")
    public String getTopics(
            @PathVariable User user,
            Model model
    ) {
        model.addAttribute("topicResults", topicResultService.getTopics(user));
        model.addAttribute("topicList", topicService.getTopics(user));
        model.addAttribute("user", user);
        model.addAttribute("usersTab", "active");
        return "medical/result/topicList";
    }

    @GetMapping("/{userId}/newTopic/{topic}")
    public String newTopic(
            @PathVariable Long userId,
            @PathVariable MedicalTopic topic,
            RedirectAttributes redirectAttributes
    ) {
        topicInvokeService.startTopic(userId, topic);
        redirectAttributes.addAttribute("userId", userId);
        return "redirect:/result/topic/{userId}";
    }

    @GetMapping("/{userId}/delete/{topicResultId}")
    public String deleteTopicResult(
            @PathVariable Long userId,
            @PathVariable Long topicResultId,
            RedirectAttributes redirectAttributes
    ) {
        topicResultService.deleteResult(userId, topicResultId);
        redirectAttributes.addAttribute("userId", userId);
        return "redirect:/result/topic/{userId}";
    }

    @GetMapping("/{userId}/{topicResultId}")
    public String getTopicResult(
            @PathVariable Long userId,
            @PathVariable Long topicResultId,
            Model model
    ) {
        model.addAttribute("result", topicResultService.getResult(topicResultId));
        model.addAttribute("userId", userId);
        model.addAttribute("usersTab", "active");
        return "medical/result/topicResult";
    }

    @PostMapping("/updateTaskResult/{userId}/{topicResult}/{taskResult}")
    public String updateTaskResult(
            @PathVariable Long userId,
            @PathVariable MedicalTopicResult topicResult,
            @PathVariable MedicalTaskResult taskResult,
            @RequestParam Float altScore
    ) {
        taskResult.setAltScore(altScore);
        topicResultService.updateTaskResult(taskResult);
        return "redirect:/result/topic/{userId}/{topicResult}";
    }

}
