package Filters;

import entity.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class UserFilter implements Filter {

    private FilterConfig config;

    public void destroy() {
        config = null;
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws ServletException, IOException {

        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpSession session = request.getSession();
        User user = (User)session.getAttribute("user");
        if (user == null) {
            request.setAttribute("errorMessage",  "Access denied. Log in, please.");
            RequestDispatcher dispatcher =
                    request.getRequestDispatcher("WEB-INF/jsp/error.jsp");
            dispatcher.forward(servletRequest, servletResponse);
        } else {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    public void init(FilterConfig filterConfig) throws ServletException {

        config = filterConfig;
    }

}
