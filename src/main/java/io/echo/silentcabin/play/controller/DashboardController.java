package io.echo.silentcabin.play.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/dashboard")
public class DashboardController {
    @GetMapping
    public String index(@RequestParam String accessToken, String refreshToken, Model model) {
        model.addAttribute("accessToken", accessToken);
        model.addAttribute("refreshToken", refreshToken);
        return "dashboard";
    }

}
