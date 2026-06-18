package io.echo.silentcabin.play.controller;

import io.echo.silentcabin.play.dto.GamePlaySuccessRateInfo;
import io.echo.silentcabin.play.service.GamePlayService;
import io.echo.silentcabin.user.dto.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/dashboard")
public class DashboardController {

    private final GamePlayService playService;

    //성공률 차트
    @GetMapping("/success-rate")
    public String showSuccessRate(@AuthenticationPrincipal AuthUser authUser, Model model) {
        GamePlaySuccessRateInfo info = playService.showSuccessRate(authUser);
        model.addAttribute("clearCount", info.successCount());
        model.addAttribute("failCount", info.failCount());
        return "dashboard/successRate";
    }
}
