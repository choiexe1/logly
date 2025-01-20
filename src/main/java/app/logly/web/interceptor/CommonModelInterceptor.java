package app.logly.web.interceptor;

import app.logly.domain.Member;
import app.logly.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@RequiredArgsConstructor
public class CommonModelInterceptor implements HandlerInterceptor {
    private final MemberService memberService;

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        if (!request.getMethod().equals("POST") && modelAndView != null) {
            Map<String, Object> model = modelAndView.getModel();

            model.put("member", getMember(request));
            model.put("currentUri", request.getRequestURI());
        }
    }

    private Member getMember(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session != null) {
            Long id = (Long) session.getAttribute("id");
            return memberService.findByIdOrThrow(id);
        }

        return null;
    }

}