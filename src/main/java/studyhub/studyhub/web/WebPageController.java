package studyhub.studyhub.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.ui.Model;

@Controller
// 서버 렌더링 템플릿 진입 경로를 제공한다.
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

    @GetMapping("/friends")
    public String friends() {
        return "friend";
    }

    @GetMapping("/chats")
    public String chats() {
        return "chat";
    }

    @GetMapping("/studies/{studyId}")
    public String studyDetail(@PathVariable Long studyId, Model model) {
        model.addAttribute("studyId", studyId);
        return "study-detail";
    }
}
