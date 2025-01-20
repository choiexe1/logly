package app.logly.web.controller;

import app.logly.domain.Member;
import app.logly.exception.EmailInUsedException;
import app.logly.exception.InvalidPasswordException;
import app.logly.exception.NicknameInUsedException;
import app.logly.exception.PasswordMismatchException;
import app.logly.exception.UserNotFoundException;
import app.logly.exception.UsernameInUsedException;
import app.logly.exception.VerificationCodeNotMatchException;
import app.logly.helper.mail.MailHelper;
import app.logly.service.AuthService;
import app.logly.service.MemberService;
import app.logly.service.VerificationService;
import app.logly.web.annotation.ReturnTemplateOnError;
import app.logly.web.form.LoginForm;
import app.logly.web.form.RegisterForm;
import app.logly.web.form.VerificationCodeForm;
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
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/")
@Slf4j
@RequiredArgsConstructor
public class HomeController {
    private final AuthService authService;
    private final MemberService memberService;
    private final MailHelper mailHelper;
    private final VerificationService verificationService;

    @GetMapping("/login")
    public String loginView(HttpServletRequest request, @ModelAttribute("form") LoginForm form) {
        HttpSession session = request.getSession(false);

        if (session != null && session.getAttribute("id") != null) {
            return "redirect:/";
        }

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
            session.setAttribute("id", member.getId());

            if (member.isEmailVerified()) {
                return "redirect:/";
            } else {
                return "redirect:/verify";
            }

        } catch (UserNotFoundException e) {
            bindingResult.rejectValue("username", UserNotFoundException.ERROR_CODE);
            return "login";
        } catch (InvalidPasswordException e) {
            bindingResult.rejectValue("password", InvalidPasswordException.ERROR_CODE);
            return "login";
        }
    }

    @GetMapping("/verify")
    public String verifyView(Model model, @SessionAttribute("id") Long id,
                             @ModelAttribute("form") VerificationCodeForm form) {
        Member member = memberService.findByIdOrThrow(id);
        model.addAttribute("email", member.getEmail());

        return "verify";
    }

    @PostMapping("/verify")
    public String verify(@SessionAttribute("id") Long id, @Validated @ModelAttribute("form") VerificationCodeForm form,
                         BindingResult bindingResult) {

        if (form.first() == null || form.second() == null || form.third() == null || form.fourth() == null) {
            bindingResult.reject(VerificationCodeNotMatchException.ERROR_CODE);
            return "verify";
        }

        try {
            verificationService.verify(id, form.merge());
            authService.verifyEmail(id);
        } catch (VerificationCodeNotMatchException e) {
            bindingResult.reject(VerificationCodeNotMatchException.ERROR_CODE);
            return "verify";
        }

        return "redirect:/";
    }

    @GetMapping("re-send")
    public String verifyResend(@SessionAttribute("id") Long id, RedirectAttributes redirectAttributes) {
        Member member = memberService.findByIdOrThrow(id);

        int verificationCode = verificationService.regenerateVerificationCode(member.getId());
        mailHelper.sendVerifyCode(member.getEmail(), verificationCode);

        redirectAttributes.addFlashAttribute("resend", true);

        return "redirect:/verify";
    }

    @GetMapping("/register")
    public String registerView(HttpServletRequest request, @ModelAttribute("form") RegisterForm form) {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("id") != null) {
            return "redirect:/";
        }

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

            int verificationCode = verificationService.generateVerificationCode(member.getId());
            mailHelper.sendVerifyCode(form.email(), verificationCode);

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
    public String home() {
        return "home";
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        session.invalidate();

        return "redirect:/login";
    }

    @GetMapping("/terms")
    public String termsView() {
        return "terms";
    }
}
