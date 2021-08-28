package webshop;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;

@Controller
public class SessionController {

    protected static final List<Session> activeSessions = new ArrayList<>();

    public Session getOrSetSession(String sessID, HttpServletResponse response) {
        for (Session session : activeSessions) {
            if (session.getId().equals(sessID)) {
                return session;
            }
        }
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 23;

        boolean goOn = false;
        while (!goOn) {
            sessID = new Random().ints(leftLimit, rightLimit + 1)
                    .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97)).limit(targetStringLength)
                    .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();

            goOn = !idExists(sessID);
        }

        Cookie cookie = new Cookie("SessionID", sessID);
        cookie.setPath("/");
        response.addCookie(cookie);
        Session session = new Session(sessID);
        activeSessions.add(session);
        return session;
    }

    private boolean idExists(String sessID) {
        for (Session session : activeSessions) {
            if (session.getId().equals(sessID)) {
                return true;
            }
        }
        return false;
    }
}