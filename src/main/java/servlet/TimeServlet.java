package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(value = "/time")
public class TimeServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");

        try (PrintWriter out = response.getWriter()) {

            String timezoneParam = request.getParameter("timezone");
            String timezone = (timezoneParam != null && !timezoneParam.isEmpty()) ? timezoneParam : "UTC";

            if (!isValidTimezone(timezone)) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid timezone");
                return;
            }

            LocalDateTime currentTime = LocalDateTime.now(ZoneId.of(timezone));
            ZonedDateTime currentZonedDateTime = currentTime.atZone(ZoneId.of(timezone));

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z");
            String formattedTime = currentZonedDateTime.format(formatter);

            out.println("<html><body>");
            out.println("<h1>Поточний час</h1>");
            out.println("<p>" + formattedTime + "</p>");
            out.println("<p>Часовий пояс: " + timezone + "</p>");
            out.println("</body></html>");
        }
    }

    private boolean isValidTimezone(String timezone) {
        return ZoneId.getAvailableZoneIds().contains(timezone);
    }
}
