package studyhub.studyhub.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.ui.Model;

@Controller
public class WebPageController {

    @GetMapping("/")
    public String index() {
        return "intro";
    }

    @GetMapping("/auth")
    public String auth() {
        return "auth";
    }

    @GetMapping("/main")
    public String main() {
        return "main";
    }

    @GetMapping("/studies/{studyId}")
    public String studyDetail(@PathVariable Long studyId, Model model) {
        model.addAttribute("studyId", studyId);
        return "study-detail";
    }
}
