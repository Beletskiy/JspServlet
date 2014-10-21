package servlets;

import dao.impl.JdbcUserDao;
import entity.User;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DeleteUserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String login = request.getParameter("login");
		try {
			User userToDelete = new User();
			userToDelete.setLogin(login);
			new JdbcUserDao().remove(userToDelete);
			response.sendRedirect("AdminServlet");
		} 
		catch (Exception e) {
			request.setAttribute("errorMessage", e);
			RequestDispatcher dispatcher = request
					.getRequestDispatcher("WEB-INF/jsp/error.jsp");
			dispatcher.forward(request, response);
		}
	}
}
