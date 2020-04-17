package client.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class PageController {

    @GetMapping("/admin")
    public String getAdminPage() {
        return "usersList";
    }

    @GetMapping("/user")
    public String getUserPage() {
        return "userPage";
    }

    @GetMapping("/")
    public String testGetHome() {
        return "HomePage";
    }
}
