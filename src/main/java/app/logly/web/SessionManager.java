package app.logly.web;


import jakarta.servlet.http.HttpSession;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class SessionManager {
    private static final Map<String, HttpSession> sessionMap = new ConcurrentHashMap<>();

    private SessionManager() {
    }
    
    public static void set(String username, HttpSession session) {
        sessionMap.put(username, session);
    }

    public static Optional<HttpSession> get(String username) {
        return Optional.ofNullable(sessionMap.get(username));
    }

    public static void remove(String username) {
        sessionMap.remove(username);
    }
}