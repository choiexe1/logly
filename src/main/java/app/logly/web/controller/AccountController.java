package app.logly.web.controller;

import app.logly.domain.Member;
import app.logly.exception.NicknameInUsedException;
import app.logly.service.MemberService;
import app.logly.web.form.EditAccountForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

@Controller
@RequestMapping("/accounts")
@RequiredArgsConstructor
@Slf4j
public class AccountController {
    private final MemberService memberService;

    @GetMapping
    public String accountEditView(@SessionAttribute("id") Long id, @ModelAttribute("form") EditAccountForm form) {
        Member member = memberService.findByIdOrThrow(id);

        form.setNickname(member.getNickname());
        form.setEmail(member.getEmail());

        return "accounts/account";
    }

    @PostMapping
    public String accountEdit(
            @SessionAttribute("id") Long id,
            @Validated @ModelAttribute("form") EditAccountForm form,
            BindingResult bindingResult
    ) {
        try {
            memberService.update(id, form.getNickname());
        } catch (NicknameInUsedException e) {
            bindingResult.rejectValue("nickname", NicknameInUsedException.ERROR_CODE);
            return "accounts/account";
        }
        
        return "redirect:/accounts";
    }
}
