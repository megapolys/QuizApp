package com.example.servingwebcontent.controller.medical;

import com.example.servingwebcontent.domain.medical.MedicalTask;
import com.example.servingwebcontent.domain.medical.MedicalTopic;
import com.example.servingwebcontent.service.DecisionService;
import com.example.servingwebcontent.service.medical.MedicalTopicService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Comparator;

@Controller
@RequestMapping("/medical")
@PreAuthorize("hasAuthority('ADMIN')")
public class MedicalController {

    private final MedicalTopicService medicalTopicService;
    private final DecisionService decisionService;

    public MedicalController(MedicalTopicService medicalTopicService, DecisionService decisionService) {
        this.medicalTopicService = medicalTopicService;
        this.decisionService = decisionService;
    }

    @GetMapping("/list")
    public String medicalList(
            Model model
    ) {
        model.addAttribute("topics", medicalTopicService.sortedTopics());
        model.addAttribute("medicalAdminTab", "active");
        return "medical/admin/topicList";
    }

    @GetMapping("/add")
    public String addTopic(
            @RequestParam String name,
            RedirectAttributes redirectAttributes
    ) {
        addOrUpdate(new MedicalTopic(), name, redirectAttributes, false);
        return "redirect:/medical/list";
    }

    @GetMapping("/copy/{topic}")
    public String copyTopic(
            @PathVariable MedicalTopic topic,
            RedirectAttributes redirectAttributes
    ) {
        if (medicalTopicService.copy(topic)) {
            redirectAttributes.addFlashAttribute("successMessage", "Анализы скопированы успешно");
        } else {
            redirectAttributes.addFlashAttribute("message", "Имя анализа слишком длинное для копирования!");
        }
        return "redirect:/medical/list";
    }

    @GetMapping("/rename/{topic}")
    public String renameTopic(
            @PathVariable MedicalTopic topic,
            @RequestParam String name,
            RedirectAttributes redirectAttributes
    ) {
        addOrUpdate(topic, name, redirectAttributes, true);
        return "redirect:/medical/{topic}";
    }

    private void addOrUpdate(MedicalTopic topic, String name, RedirectAttributes redirectAttributes, boolean update) {
        name = name.trim();
        if (StringUtils.isBlank(name)) {
            redirectAttributes.addFlashAttribute("message", "Имя медицинского топика пустое!");
        } else if (medicalTopicService.contains(name)) {
            redirectAttributes.addFlashAttribute("message", "Топик с таким именем уже существует!");
        } else {
            topic.setName(name);
            medicalTopicService.save(topic);
            redirectAttributes.addFlashAttribute("successMessage", String.format("Медицинский топик '%s' успешно %s.",
                    name, update ? "обновлен" : "добавлен"));
        }
    }

    @GetMapping("/delete/{topic}")
    public String delete(
            @PathVariable MedicalTopic topic
    ) {
        medicalTopicService.delete(topic);
        return "redirect:/medical/list";
    }

    @GetMapping("/{topic}")
    public String topic(
            @PathVariable MedicalTopic topic,
            Model model
    ) {
        model.addAttribute("topic", topic);
        model.addAttribute("taskList", topic.getMedicalTasks().stream()
                .sorted(Comparator.comparing(MedicalTask::getId)).toList());
        model.addAttribute("groups", decisionService.groups());
        model.addAttribute("decisions", decisionService.decisionsWithoutGroups());
        return "/medical/admin/topic";
    }

    @PostMapping("/{topic}/task/add")
    public String addTask(
            @PathVariable MedicalTopic topic,
            MedicalTask task,
            RedirectAttributes redirectAttributes
    ) {
        if (validateTask(task, redirectAttributes)) {
            if (medicalTopicService.addTask(topic, task)) {
                redirectAttributes.addFlashAttribute("successMessage", "Анализ успешно добавлен.");
            } else {
                redirectAttributes.addFlashAttribute("task", task);
                redirectAttributes.addFlashAttribute("message", "В данном топике анализ с таким именем уже существует!");
            }
        } else {
            redirectAttributes.addFlashAttribute("task", task);
        }
        return "redirect:/medical/{topic}";
    }

    @GetMapping("/{topic}/task/update/{task}")
    public String updateTaskInit(
            @PathVariable MedicalTopic topic,
            @PathVariable MedicalTask task,
            RedirectAttributes redirectAttributes
    ) {
        redirectAttributes.addFlashAttribute("task", task);
        redirectAttributes.addFlashAttribute("update", "update");
        return "redirect:/medical/{topic}";
    }

    @PostMapping("/{topic}/task/update")
    public String updateTask(
            @PathVariable MedicalTopic topic,
            MedicalTask task,
            RedirectAttributes redirectAttributes
    ) {
        if (validateTask(task, redirectAttributes)) {
            if (medicalTopicService.updateTask(topic, task)) {
                redirectAttributes.addFlashAttribute("successMessage", "Анализ успешно обновлен.");
            } else {
                redirectAttributes.addFlashAttribute("update", "update");
                redirectAttributes.addFlashAttribute("task", task);
                redirectAttributes.addFlashAttribute("message", "В данном топике анализ с таким именем уже существует!");
            }
        } else {
            redirectAttributes.addFlashAttribute("update", "update");
            redirectAttributes.addFlashAttribute("task", task);
        }
        return "redirect:/medical/{topic}";
    }

    private boolean validateTask(MedicalTask task, RedirectAttributes redirectAttributes) {
        boolean result = true;
        final StringBuilder builder = new StringBuilder();
        if (StringUtils.isBlank(task.getName())) {
            builder.append("Пустое название анализа. ");
            result = false;
        }
        if (task.getLeftLeft() == null || task.getLeftMid() == null || task.getRightMid() == null || task.getRightRight() == null) {
            builder.append("Есть незаполненные поля граничных значений.");
            result = false;
        } else if (!(task.getLeftLeft() < task.getLeftMid() && task.getLeftMid() < task.getRightMid() && task.getRightMid() < task.getRightRight())) {
            builder.append("Значения границ нарушают логику.");
            result = false;
        }
        if (!result) {
            redirectAttributes.addFlashAttribute("message", builder.toString());
        }
        return result;
    }

    @GetMapping("/{topic}/task/delete/{task}")
    public String deleteTask(
            @PathVariable MedicalTopic topic,
            @PathVariable MedicalTask task,
            RedirectAttributes redirectAttributes
    ) {
        medicalTopicService.deleteTask(task);
        return "redirect:/medical/{topic}";
    }

}

