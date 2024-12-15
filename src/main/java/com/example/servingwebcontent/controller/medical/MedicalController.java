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
//
//    @PostMapping("/{topic}/task/add")
//    public String addTask(
//            @PathVariable MedicalTopicWithTaskSize topic,
//            MedicalTask task,
//            RedirectAttributes redirectAttributes
//    ) {
//        if (validateTask(task, redirectAttributes)) {
//            if (medicalTopicService.addTask(topic, task)) {
//                redirectAttributes.addFlashAttribute("successMessage", "Анализ успешно добавлен.");
//            } else {
//                redirectAttributes.addFlashAttribute("task", task);
//                redirectAttributes.addFlashAttribute("message", "В данном топике анализ с таким именем уже существует!");
//            }
//        } else {
//            redirectAttributes.addFlashAttribute("task", task);
//        }
//        return "redirect:/medical/{topic}";
//    }
//
//    @GetMapping("/{topic}/task/update/{task}")
//    public String updateTaskInit(
//            @PathVariable MedicalTopicWithTaskSize topic,
//            @PathVariable MedicalTask task,
//            RedirectAttributes redirectAttributes
//    ) {
//        redirectAttributes.addFlashAttribute("task", task);
//        redirectAttributes.addFlashAttribute("update", "update");
//        return "redirect:/medical/{topic}";
//    }
//
//    @PostMapping("/{topic}/task/update")
//    public String updateTask(
//            @PathVariable MedicalTopicWithTaskSize topic,
//            MedicalTask task,
//            RedirectAttributes redirectAttributes
//    ) {
//        if (validateTask(task, redirectAttributes)) {
//            if (medicalTopicService.updateTask(topic, task)) {
//                redirectAttributes.addFlashAttribute("successMessage", "Анализ успешно обновлен.");
//            } else {
//                redirectAttributes.addFlashAttribute("update", "update");
//                redirectAttributes.addFlashAttribute("task", task);
//                redirectAttributes.addFlashAttribute("message", "В данном топике анализ с таким именем уже существует!");
//            }
//        } else {
//            redirectAttributes.addFlashAttribute("update", "update");
//            redirectAttributes.addFlashAttribute("task", task);
//        }
//        return "redirect:/medical/{topic}";
//    }
//
//    private boolean validateTask(MedicalTask task, RedirectAttributes redirectAttributes) {
//        boolean result = true;
//        final StringBuilder builder = new StringBuilder();
//        if (StringUtils.isBlank(task.getName())) {
//            builder.append("Пустое название анализа. ");
//            result = false;
//        }
//        if (task.getLeftLeft() == null || task.getLeftMid() == null || task.getRightMid() == null || task.getRightRight() == null) {
//            builder.append("Есть незаполненные поля граничных значений.");
//            result = false;
//        } else if (!(task.getLeftLeft() < task.getLeftMid() && task.getLeftMid() < task.getRightMid() && task.getRightMid() < task.getRightRight())) {
//            builder.append("Значения границ нарушают логику.");
//            result = false;
//        }
//        if (!result) {
//            redirectAttributes.addFlashAttribute("message", builder.toString());
//        }
//        return result;
//    }
//
//    @PostMapping("/{topic}/task/delete/{task}")
//    public String deleteTask(
//            @PathVariable MedicalTopicWithTaskSize topic,
//            @PathVariable MedicalTask task,
//            RedirectAttributes redirectAttributes
//    ) {
//        medicalTopicService.deleteTask(task);
//        redirectAttributes.addFlashAttribute("successMessage", "Анализ успешно удален");
//        return "redirect:/medical/{topic}";
//    }
}

