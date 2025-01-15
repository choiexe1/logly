package app.logly.web.controller;

import app.logly.domain.Member;
import app.logly.exception.EmailInUsedException;
import app.logly.exception.InvalidPasswordException;
import app.logly.exception.NicknameInUsedException;
import app.logly.exception.PasswordMismatchException;
import app.logly.exception.UserNotFoundException;
import app.logly.exception.UsernameInUsedException;
import app.logly.service.AuthService;
import app.logly.web.SessionManager;
import app.logly.web.annotation.ReturnTemplateOnError;
import app.logly.web.form.LoginForm;
import app.logly.web.form.RegisterForm;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
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
            authService.authenticate(username, password);
        } catch (UserNotFoundException e) {
            bindingResult.rejectValue("username", UserNotFoundException.ERROR_CODE);
            return "login";
        } catch (InvalidPasswordException e) {
            bindingResult.rejectValue("password", InvalidPasswordException.ERROR_CODE);
            return "login";
        }

        Optional<HttpSession> session = SessionManager.get(username);

        if (session.isPresent()) {
            session.get().invalidate();
            SessionManager.remove(username);
        }

        SessionManager.set(username, request.getSession());

        return "redirect:/";
    }

    @GetMapping("/register")
    public String registerView(@ModelAttribute("form") RegisterForm form) {
        return "register";
    }

    // TODO: 로그인 후에 인터셉터로 세션 검증

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

        redirectAttributes.addFlashAttribute("registered", true);
        return "redirect:/login";
    }

    @GetMapping("/terms")
    public String termsView() {
        return "terms";
    }
}
