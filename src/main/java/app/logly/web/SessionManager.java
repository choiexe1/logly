package app.logly.web;


import jakarta.servlet.http.HttpSession;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SessionManager {
    private static final Map<Long, HttpSession> sessionMap = new ConcurrentHashMap<>();

    private SessionManager() {
    }

    public static void set(Long id, HttpSession session) {
        session.setAttribute("id", id);
        sessionMap.put(id, session);
    }

    public static HttpSession get(Long id) {
        return sessionMap.get(id);
    }

    public static void remove(Long id) {
        sessionMap.remove(id);
    }

    public static boolean isDuplicateLogin(Long id, HttpSession session) {
        HttpSession existingSession = sessionMap.get(id);
        return existingSession != null && !existingSession.equals(session);
    }

    public static void renewSession(Long id, HttpSession session) {
        boolean duplicateLogin = isDuplicateLogin(id, session);

        if (duplicateLogin) {
            HttpSession oldSession = sessionMap.get(id);
            oldSession.invalidate();
            set(id, session);
        } else {
            set(id, session);
        }
    }
}