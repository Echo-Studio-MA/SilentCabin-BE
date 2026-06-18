package io.echo.silentcabin.play.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    @GetMapping
    public String index(Model model) {
        model.addAttribute("clearCount", 73);
        model.addAttribute("failCount", 27);
        return "dashboard/index";
    }
}
