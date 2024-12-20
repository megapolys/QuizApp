package com.example.servingwebcontent.controller.medical;

import com.example.servingwebcontent.model.medical.result.MedicalTaskResult;
import com.example.servingwebcontent.model.medical.result.MedicalTopicResult;
import com.example.servingwebcontent.model.user.User;
import com.example.servingwebcontent.service.medical.MedicalTopicInvokeService;
import io.micrometer.common.util.StringUtils;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class MedicalInvokeController {

    private final MedicalTopicInvokeService topicInvokeService;

    public MedicalInvokeController(MedicalTopicInvokeService topicInvokeService) {
        this.topicInvokeService = topicInvokeService;
    }

    @GetMapping("/userTopicList")
    public String topicList(
            @AuthenticationPrincipal User user,
            Model model
    ) {
        model.addAttribute("topics", topicInvokeService.getTopicResults(user.getId()));
        model.addAttribute("invokeTopicTab", "active");
        return "medical/invoke/topicList";
    }

    @GetMapping("/invokeTopic/{topicResult}")
    public String invokeTopic(
            @AuthenticationPrincipal User user,
            @PathVariable MedicalTopicResult topicResult,
            Model model
    ) {
        if (topicInvokeService.userNotContainsQuiz(user.getId(), topicResult.getId())) {
            throw new RuntimeException("Access denied!");
        }
        topicResult.setResults(topicResult.getResults().stream()
                .sorted(Comparator.comparing(r -> r.getMedicalTask().getId()))
                .collect(Collectors.toCollection(LinkedHashSet::new)));
        model.addAttribute("topicResult", topicResult);
        return "medical/invoke/topicInvoke";
    }

    @PostMapping("/completeTopic/{topicResult}")
    public String completeTopic(
            @AuthenticationPrincipal User user,
            @PathVariable MedicalTopicResult topicResult,
            @RequestParam Map<String, String> params
    ) {
        if (topicInvokeService.userNotContainsQuiz(user.getId(), topicResult.getId())) {
            throw new RuntimeException("Access denied!");
        }
        boolean changed = false;
        for (MedicalTaskResult result : topicResult.getResults()) {
            final String newVal = params.get(result.getId().toString());
            if (StringUtils.isNotBlank(newVal)) {
                final float value = Float.parseFloat(newVal);
                if (result.getValue() == null || value != result.getValue()) {
                    changed = true;
                    result.setValue(value);
                }
            } else {
                if (result.getValue() != null) {
                    changed = true;
                    result.setValue(null);
                }
            }
        }
        if (changed) {
            topicInvokeService.save(topicResult);
        }
        return "redirect:/userTopicList";
    }
}
