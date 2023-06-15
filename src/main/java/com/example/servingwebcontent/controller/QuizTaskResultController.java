package com.example.servingwebcontent.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/result/task")
@PreAuthorize("hasAuthority('ADMIN')")
public class QuizTaskResultController {
}
