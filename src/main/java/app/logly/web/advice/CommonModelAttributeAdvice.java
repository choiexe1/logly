package app.logly.web.advice;

import app.logly.domain.Member;
import app.logly.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@ControllerAdvice
@RequiredArgsConstructor
public class CommonModelAttributeAdvice {
    private final MemberService memberService;

    @ModelAttribute
    public void addCommonAttributes(@SessionAttribute(value = "id", required = false) Long id, Model model) {
        if (id != null) {
            Member member = memberService.findByIdOrThrow(id);
            model.addAttribute("member", member);
        }

        model.addAttribute("currentUri", ServletUriComponentsBuilder.fromCurrentRequest().toUriString());
    }
}
