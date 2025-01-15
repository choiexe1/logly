package app.logly.web.controller;

import app.logly.domain.Member;
import app.logly.service.MemberService;
import app.logly.web.form.LoginForm;
import app.logly.web.form.RegisterForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/")
@Slf4j
@RequiredArgsConstructor
public class HomeController {
    private final MemberService memberService;

    @GetMapping("/login")
    public String loginView(@ModelAttribute("form") LoginForm form) {
        return "login";
    }

    @GetMapping("/register")
    public String registerView(@ModelAttribute("form") RegisterForm form) {
        return "register";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute("form") LoginForm form) {
        return "redirect:/";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute("form") RegisterForm form,
                           RedirectAttributes redirectAttributes) {
        Member member = Member
                .of(form.username(), form.nickname(), form.email(), form.password(),
                        form.subscribeNewsletter());
        memberService.save(member);

        redirectAttributes.addFlashAttribute("registerd", true);

        return "redirect:/login";
    }

    @GetMapping("/terms")
    public String termsView() {
        return "terms";
    }
}
