package servlets;

import dao.impl.JdbcUserDao;
import entity.User;
import interfaces.UserDao;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LoginServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String login = request.getParameter("login");
		String password = request.getParameter("password");
		Map<String, String> errors = validateLogin(login, password);

		// login and/or password fields are empty
		if (!errors.isEmpty()) {
			request.setAttribute("login", login);
			request.setAttribute("errors", errors);
			dispatchTo("index.jsp", request, response);
			return;
		}
		UserDao userDao = new JdbcUserDao();
		User user ;

		try {
			user = userDao.findByLogin(login);
		} catch (Exception e) {
			request.setAttribute("errorMessage", e);
			dispatchTo("WEB-INF/jsp/error.jsp", request, response);
			return;
		}
		// user doesn't exists
		if (user == null || !user.getPassword().equals(password)) {
			dispatchTo("WEB-INF/jsp/no-user.jsp", request, response);
			return;
		}

		HttpSession session = request.getSession();
		session.setAttribute("user", user);

		// check out the role of the user and make corresponding redirect
		String role = user.getRole().getName();
		if (role.equals("user")) {
			response.sendRedirect("UserServlet");
		} else { 
			response.sendRedirect("AdminServlet");
		}
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.sendRedirect("index.jsp");
	}

	private void dispatchTo(String path, HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {
		RequestDispatcher dispatcher = req.getRequestDispatcher(path);
		dispatcher.forward(req, resp);
	}

	private Map<String, String> validateLogin(String login, String password) {
		Map<String, String> errors = new HashMap<String, String>();
		login = login.trim();
		password = password.trim();

		if (login.isEmpty()) {
			errors.put("login", "Please, enter your login");
		}

		if (password.isEmpty()) {
			errors.put("password", "Please, enter your password");
		}

		return errors;
	}

}
