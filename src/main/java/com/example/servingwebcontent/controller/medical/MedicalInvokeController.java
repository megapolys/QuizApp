package com.example.servingwebcontent.controller.medical;

import com.example.servingwebcontent.service.medical.impl.MedicalTopicInvokeServiceImpl;
import org.springframework.stereotype.Controller;

@Controller
public class MedicalInvokeController {

	private final MedicalTopicInvokeServiceImpl topicInvokeService;

	public MedicalInvokeController(MedicalTopicInvokeServiceImpl topicInvokeService) {
		this.topicInvokeService = topicInvokeService;
	}

//    @GetMapping("/userTopicList")
//    public String topicList(
//            @AuthenticationPrincipal User user,
//            Model model
//    ) {
//        model.addAttribute("topics", topicInvokeService.getTopicResults(user.getId()));
//        model.addAttribute("invokeTopicTab", "active");
//        return "medical/invoke/topicList";
//    }
//
//    @GetMapping("/invokeTopic/{topicResult}")
//    public String invokeTopic(
//            @AuthenticationPrincipal User user,
//            @PathVariable MedicalTopicResult topicResult,
//            Model model
//    ) {
////        if (topicInvokeService.userNotContainsQuiz(user.getId(), topicResult.getId())) {
////            throw new RuntimeException("Access denied!");
////        }
////        topicResult.setResults(topicResult.getResults().stream()
////                .sorted(Comparator.comparing(r -> r.getMedicalTask().getId()))
////                .collect(Collectors.toCollection(LinkedHashSet::new)));
////        model.addAttribute("topicResult", topicResult);
//        return "topic";
//    }
//
//    @PostMapping("/completeTopic/{topicResult}")
//    public String completeTopic(
//            @AuthenticationPrincipal User user,
//            @PathVariable MedicalTopicResult topicResult,
//            @RequestParam Map<String, String> params
//    ) {
////        if (topicInvokeService.userNotContainsQuiz(user.getId(), topicResult.getId())) {
////            throw new RuntimeException("Access denied!");
////        }
////        boolean changed = false;
////        for (MedicalTaskResult result : topicResult.getResults()) {
////            final String newVal = params.get(result.getId().toString());
////            if (StringUtils.isNotBlank(newVal)) {
////                final float value = Float.parseFloat(newVal);
////                if (result.getValue() == null || value != result.getValue()) {
////                    changed = true;
////                    result.setValue(value);
////                }
////            } else {
////                if (result.getValue() != null) {
////                    changed = true;
////                    result.setValue(null);
////                }
////            }
////        }
////        if (changed) {
////            topicInvokeService.save(topicResult);
////        }
//        return "redirect:/userTopicList";
//    }
}
