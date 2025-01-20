package app.logly.web.controller;

import app.logly.domain.Guestbook;
import app.logly.domain.Member;
import app.logly.service.GuestbookService;
import app.logly.service.MemberService;
import app.logly.web.form.PostGuestbookForm;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/guestbook")
@Slf4j
@RequiredArgsConstructor
public class GuestbookController {
    private final GuestbookService guestbookService;
    private final MemberService memberService;

    @GetMapping("/@{nickname}")
    public String guestbookView(@PathVariable("nickname") String nickname, Model model, @ModelAttribute("form")
    PostGuestbookForm form) {
        Member member = memberService.findByNicknameOrThrow(nickname);
        List<Guestbook> allByHostId = guestbookService.findAllByHostId(member.getId());
        model.addAttribute("guestbooks", allByHostId);

        return "guestbook/index";
    }

    @GetMapping("/posted")
    public String postedView(@SessionAttribute("id") Long id, Model model) {
        List<Guestbook> allByGuestId = guestbookService.findAllByGuestId(id);
        model.addAttribute("guestbooks", allByGuestId);
        return "guestbook/posted";
    }

    @PostMapping("/@{nickname}")
    public String post(@PathVariable("nickname") String nickname, @SessionAttribute("id") Long guestId, Model model,
                       @Validated @ModelAttribute("form") PostGuestbookForm form, BindingResult bindingResult,
                       RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            Member member = memberService.findByNicknameOrThrow(nickname);
            List<Guestbook> allByHostId = guestbookService.findAllByHostId(member.getId());

            model.addAttribute("nickname", nickname);
            model.addAttribute("guestbooks", allByHostId);
            return "guestbook/index";
        }

        Member host = memberService.findByNicknameOrThrow(nickname);
        Member guest = memberService.findByIdOrThrow(guestId);

        Guestbook guestbook = Guestbook.of(form.content());
        guestbook.setHost(host);
        guestbook.setGuest(guest);

        guestbookService.save(guestbook);

        redirectAttributes.addAttribute("nickname", nickname);
        return "redirect:/guestbook/@{nickname}";
    }
}
