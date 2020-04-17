package client.controller;

import client.model.User;
import client.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class OauthLoginController {
    private UserService userService;

    @Autowired
    public OauthLoginController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/oauth")
    public String loginOauth(@AuthenticationPrincipal User user) {
        userService.oauthLogin(user);
        return "redirect:/user";
    }

}
