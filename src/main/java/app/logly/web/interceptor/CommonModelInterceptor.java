package app.logly.web.interceptor;

import app.logly.domain.Member;
import app.logly.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@RequiredArgsConstructor
public class CommonModelInterceptor implements HandlerInterceptor {
    private final MemberService memberService;

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        if (modelAndView != null) {
            modelAndView.addObject("member", getMember(request));
            modelAndView.addObject("currentUri", request.getRequestURI());
        }
    }

    private Member getMember(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Long id = (Long) session.getAttribute("id");

        return memberService.findByIdOrThrow(id);
    }

}