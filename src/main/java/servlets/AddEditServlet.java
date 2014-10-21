package servlets;

import dao.impl.JdbcRoleDao;
import dao.impl.JdbcUserDao;
import entity.Role;
import entity.User;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class AddEditServlet extends HttpServlet {

    private Log logger = LogFactory.getLog(AddEditServlet.class.getName());

		protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		HashMap<String, String> userInfo = new HashMap<String, String>();

		String login = request.getParameter("login");
		boolean addPage = login == null ? true : false;

		if (addPage) {
			userInfo.put("page-name", "Add");
			request.setAttribute("userInfo", userInfo);
			request.getRequestDispatcher("WEB-INF/jsp/add-edit.jsp").forward(
					request, response);
		} else {
			userInfo.put("page-name", "Edit");

			User user;
			try {
				user = new JdbcUserDao().findByLogin(login);
				if (user == null) {
					throw new Exception("Login is not correct. ");
				}
			} catch (Exception e) {
				request.setAttribute("errorMessage", e);
				request.getRequestDispatcher("WEB-INF/jsp/error.jsp").forward(
						request, response);
				return;
			}

			userInfo.put("login", user.getLogin());
			userInfo.put("email", user.getEmail());
			userInfo.put("first-name", user.getFirstName());
			userInfo.put("last-name", user.getLastName());
			userInfo.put("birthday", user.getBirthday().toString());

			request.setAttribute("userInfo", userInfo);
			request.getRequestDispatcher("WEB-INF/jsp/add-edit.jsp").forward(
					request, response);

		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// get all parameters from form
		Map<String, String> userInfo = new HashMap<String, String>();
		userInfo.put("login", request.getParameter("login"));
		userInfo.put("password", request.getParameter("password"));
		userInfo.put("password-again", request.getParameter("password-again"));
		userInfo.put("email", request.getParameter("email"));
		userInfo.put("first-name", request.getParameter("first-name"));
		userInfo.put("last-name", request.getParameter("last-name"));
		userInfo.put("birthday", request.getParameter("birthday"));
		userInfo.put("role", request.getParameter("role"));
		userInfo.put("page-name", request.getParameter("page-name"));

		// validate all parameters
		Map<String, String> errors = validateUser(userInfo);
		if (!errors.isEmpty()) {
			request.setAttribute("errors", errors);
			request.setAttribute("userInfo", userInfo);
			dispatchTo("WEB-INF/jsp/add-edit.jsp", request, response);
			return;
		}

		// check out add or edit user and write changes to DB
		Role role = null;
		try {
			role = new JdbcRoleDao().findByName(userInfo.get("role"));
		} 
		catch (Exception e) {
			//NOP
		}
		User user = new User(role, userInfo.get("login"),
				userInfo.get("password"), userInfo.get("email"),
				userInfo.get("first-name"), userInfo.get("last-name"),
				makeCalendarFromString(userInfo.get("birthday")).getTime());

			if (userInfo.get("page-name").equals("Add")) {
				try {

                    logger.trace("Find by login ");

                   if (new JdbcUserDao().findByLogin(user.getLogin()) != null) {
                        errors.put("login", "Such login already exists");
                        request.setAttribute("errors", errors);
                        request.setAttribute("userInfo", userInfo);
                        dispatchTo("WEB-INF/jsp/add-edit.jsp", request, response);
                    }

                    logger.trace("Find by email ");
                    if (new JdbcUserDao().findByEmail(user.getEmail()) != null) {
                        errors.put("email", "Such email already exists");
                        request.setAttribute("errors", errors);
                        request.setAttribute("userInfo", userInfo);
                        dispatchTo("WEB-INF/jsp/add-edit.jsp", request, response);
                    }
					new JdbcUserDao().create(user);
				}
				catch (Exception e) {
					//NOP
                    logger.trace("Can't add user ");
				}
			}
            else if (userInfo.get("page-name").equals("Edit")) {
				try {
                  User userWithSuchEmail = new JdbcUserDao().findByEmail(user.getEmail());

                    if (userWithSuchEmail != null &&
                            !(userWithSuchEmail.getLogin().equals(user.getLogin()))) {

                        errors.put("email", "Such email already exists");
                        request.setAttribute("errors", errors);
                        request.setAttribute("userInfo", userInfo);
                        dispatchTo("WEB-INF/jsp/add-edit.jsp", request, response);
                    }
					new JdbcUserDao().update(user);
				}
                catch (Exception e) {
                    logger.trace("Can't edit user ");
				}
			}

		response.sendRedirect("AdminServlet");
	}

	private void dispatchTo(String path, HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {
		RequestDispatcher dispatcher = req.getRequestDispatcher(path);
		dispatcher.forward(req, resp);
	}

	private Map<String, String> validateUser(Map<String, String> userInfo) {

		Map<String, String> errors = new HashMap<String, String>();

		// check out empty values
		if (fieldsIsEmpty(userInfo, errors)) {
			// errors has information about empty fields
		} else

		// check out passwords
		if (!userInfo.get("password").equals(userInfo.get("password-again"))) {
			errors.put("password-again", "Passwords must be the same");
		} else

		// check out email
		if (!emailIsCorrect(userInfo.get("email"))) {
			errors.put("email", "Email format is not correct");
		} else

		// check out birthday 
		if (!birthdayIsCorrect(userInfo.get("birthday"))) {
			errors.put("birthday", "Format [YYYY-MM-DD] ");
		}
		return errors;
	}

	private boolean fieldsIsEmpty(Map<String, String> userInfo,
			Map<String, String> errors) {

		String errorMessage = "This field can't be empty";

		if (userInfo.get("login").isEmpty()) {
			errors.put("login", errorMessage);
		}
		if (userInfo.get("password").isEmpty()) {
			errors.put("password", errorMessage);
		}
		if (userInfo.get("password-again").isEmpty()) {
			errors.put("password-again", errorMessage);
		}
		if (userInfo.get("email").isEmpty()) {
			errors.put("email", errorMessage);
		}
		if (userInfo.get("first-name").isEmpty()) {
			errors.put("first-name", errorMessage);
		}
		if (userInfo.get("last-name").isEmpty()) {
			errors.put("last-name", errorMessage);
		}
		if (userInfo.get("birthday").isEmpty()) {
			errors.put("birthday", errorMessage);
		}

		return !errors.isEmpty();
	}

	private boolean emailIsCorrect(String email) {
		final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
				+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

		Pattern pattern = Pattern.compile(EMAIL_PATTERN);
		return pattern.matcher(email).matches();
	}

	private boolean birthdayIsCorrect(String birthday) {
		Calendar currentTime = new GregorianCalendar();
		Calendar userBirthday = makeCalendarFromString(birthday);

		if (userBirthday == null) {
			return false;
		}

		int age = currentTime.get(Calendar.YEAR)
				- userBirthday.get(Calendar.YEAR);

		return (age >0);
	}

	/**
	 * Make Calendar from String that represents date
	 * date  format [YYYY-MM-DD]         
	 * @return object type Calendar or null if the format of date is incorrect
	 */
	private Calendar makeCalendarFromString(String date) {

		String[] fullDate = date.split("-");

		int year;
		int month;
		int day;

		if (fullDate.length != 3) {
			return null;
		} else {
			try {
				year = Integer.valueOf(fullDate[0].trim());
				month = Integer.valueOf(fullDate[1].trim());
				day = Integer.valueOf(fullDate[2].trim());
			} catch (NumberFormatException e) {
				return null;
			}
		}

		if (month < 1 || month > 12 || day < 1 || day > 31) {
			return null;
		}
		Calendar calendar = new GregorianCalendar(year, month-1, day);
		return calendar;
	}

}
