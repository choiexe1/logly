package app.logly.web.controller;

import app.logly.domain.Member;
import app.logly.exception.EmailInUsedException;
import app.logly.exception.InvalidPasswordException;
import app.logly.exception.NicknameInUsedException;
import app.logly.exception.PasswordMismatchException;
import app.logly.exception.UserNotFoundException;
import app.logly.exception.UsernameInUsedException;
import app.logly.service.AuthService;
import app.logly.service.MemberService;
import app.logly.web.SessionManager;
import app.logly.web.annotation.ReturnTemplateOnError;
import app.logly.web.annotation.SID;
import app.logly.web.form.LoginForm;
import app.logly.web.form.RegisterForm;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
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
    private final AuthService authService;
    private final MemberService memberService;

    @GetMapping("/login")
    public String loginView(@ModelAttribute("form") LoginForm form) {
        return "login";
    }

    @ReturnTemplateOnError("login")
    @PostMapping("/login")
    public String login(HttpServletRequest request,
                        @Validated @ModelAttribute("form") LoginForm form,
                        BindingResult bindingResult) {
        String username = form.username();
        String password = form.password();
        try {
            Member member = authService.authenticate(username, password);

            HttpSession session = request.getSession();
            SessionManager.renewSession(member.getId(), session);

            return "redirect:/";
        } catch (UserNotFoundException e) {
            bindingResult.rejectValue("username", UserNotFoundException.ERROR_CODE);
            return "login";
        } catch (InvalidPasswordException e) {
            bindingResult.rejectValue("password", InvalidPasswordException.ERROR_CODE);
            return "login";
        }
    }

    @GetMapping("/register")
    public String registerView(@ModelAttribute("form") RegisterForm form) {
        return "register";
    }

    @ReturnTemplateOnError("register")
    @PostMapping("/register")
    public String register(@Validated @ModelAttribute("form") RegisterForm form,
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes) {

        if (!form.password().equals(form.confirmPassword())) {
            bindingResult.rejectValue("password", PasswordMismatchException.ERROR_CODE);
            bindingResult.rejectValue("confirmPassword", PasswordMismatchException.ERROR_CODE);
            return "register";
        }

        Member member = Member
                .of(form.username(), form.nickname(), form.email(), form.password(), form.subscribeNewsletter());

        try {
            authService.register(member);
            redirectAttributes.addAttribute("registered", true);
            return "redirect:/login";
        } catch (UsernameInUsedException e) {
            bindingResult.rejectValue("username", UsernameInUsedException.ERROR_CODE);
            return "register";
        } catch (NicknameInUsedException e) {
            bindingResult.rejectValue("nickname", NicknameInUsedException.ERROR_CODE);
            return "register";
        } catch (EmailInUsedException e) {
            bindingResult.rejectValue("email", EmailInUsedException.ERROR_CODE);
            return "register";
        }
    }

    @GetMapping("/")
    public String home(@SID Long id, Model model) {
        // TODO: 세션 매니저에서 조회하는 인터셉터 구현
        Member member = memberService.findById(id).orElseThrow();

        model.addAttribute("member", member);
        return "home";
    }

    @GetMapping("/terms")
    public String termsView() {
        return "terms";
    }
}
