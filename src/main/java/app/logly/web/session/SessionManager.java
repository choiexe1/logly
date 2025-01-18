package app.logly.web.session;

import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpSessionAttributeListener;
import jakarta.servlet.http.HttpSessionBindingEvent;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SessionManager implements HttpSessionAttributeListener, HttpSessionListener {
    private static final ConcurrentMap<Long, HttpSession> store = new ConcurrentHashMap<>();
    private static final String SESSION_UNIQUE_KEY = "id";

    private SessionManager() {
    }

    @Override
    public void attributeAdded(HttpSessionBindingEvent event) {
        if (event.getName().equals(SESSION_UNIQUE_KEY)) {
            Long memberId = (Long) event.getValue();
            HttpSession newSession = event.getSession();

            if (exists(memberId)) {
                HttpSession oldSession = store.get(memberId);
                store.remove(memberId);
                oldSession.invalidate();

                store.put(memberId, newSession);
            }

            store.put(memberId, newSession);
        }
    }

    private boolean exists(Long memberId) {
        HttpSession session = store.getOrDefault(memberId, null);
        return session != null;
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
        HttpSession session = event.getSession();
        Long id = (Long) session.getAttribute(SESSION_UNIQUE_KEY);
        store.remove(id);
    }
}
