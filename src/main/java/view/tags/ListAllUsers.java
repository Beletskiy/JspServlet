package view.tags;

import dao.impl.JdbcUserDao;
import entity.User;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class ListAllUsers implements Tag {

    PageContext pageContext;

    @Override
    public void setPageContext(PageContext pageContext) {
        this.pageContext = pageContext;
    }

    @Override
    public void setParent(Tag tag) {
    }

    @Override
    public Tag getParent() {
        return null;
    }

    @Override
    public int doStartTag() throws JspException {

        StringBuilder usersTable = new StringBuilder();

        // make table head
        usersTable.append("<tr>");
        usersTable.append("<th>");
        usersTable.append("Login");
        usersTable.append("</th>");
        usersTable.append("<th>");
        usersTable.append("First Name");
        usersTable.append("</th>");
        usersTable.append("<th>");
        usersTable.append("Last Name");
        usersTable.append("</th>");
        usersTable.append("<th>");
        usersTable.append("Age");
        usersTable.append("</th>");
        usersTable.append("<th>");
        usersTable.append("Role");
        usersTable.append("</th>");
        usersTable.append("<th>");
        usersTable.append("Actions");
        usersTable.append("</th>");
        usersTable.append("</tr>");

        // make table contains
        List<User> userList = null;
        try {
            userList = new JdbcUserDao().findAll();
        } catch (Exception ex) {
            RequestDispatcher requestDispatcher = pageContext.getRequest()
                    .getRequestDispatcher("WEB-INF/error.jsp");
            pageContext.getServletContext().setAttribute("errorMessage", ex);
            try {
                requestDispatcher.forward(pageContext.getRequest(),
                        pageContext.getResponse());
            } catch (IOException innerEx) {
                throw new JspException("Can't forward to error.jsp", innerEx);
            } catch (ServletException innerEx) {
                throw new JspException("Can't forward to error.jsp", innerEx);
            }
        }

        if (userList != null) {

            for (User user : userList) {
                usersTable.append("<tr>");
                usersTable.append("<td>");
                usersTable.append(user.getLogin());
                usersTable.append("</td>");
                usersTable.append("<td>");
                usersTable.append(user.getFirstName());
                usersTable.append("</td>");
                usersTable.append("<td>");
                usersTable.append(user.getLastName());
                usersTable.append("</td>");
                usersTable.append("<td>");
                usersTable.append(getAge(user.getBirthday()));
                usersTable.append("</td>");
                usersTable.append("<td>");
                usersTable.append(user.getRole().getName());
                usersTable.append("</td>");
                usersTable.append("<td>");
                usersTable.append("<a " +
                        "href=\"/JspServlet/AddEditServlet?login=");
                usersTable.append(user.getLogin());
                usersTable.append("\">Edit</a>&nbsp;");
                usersTable.append("<a " +
                        "href=\"/JspServlet/DeleteUserServlet?login=");
                usersTable.append(user.getLogin());
                usersTable.append("\" onclick=\"return "
                        + "confirm(\'Are you sure?\');\"> Delete </a>");
                usersTable.append("</td>");
                usersTable.append("</tr>");
            }
        }
        // print on the page
        try {
            pageContext.getOut().println(usersTable.toString());
        } catch (IOException ioe) {
            throw new JspException("Can't print a table of users", ioe);
        }
        return Tag.SKIP_BODY;
    }

    @Override
    public int doEndTag() throws JspException {
        return Tag.EVAL_PAGE;
    }

    @Override
    public void release() {
    }

    private int getAge(Date birthday) {
        Calendar current = new GregorianCalendar();
        Calendar user = new GregorianCalendar();
        user.setTime(birthday);
        int age = current.get(Calendar.YEAR) - user.get(Calendar.YEAR);
        return age;
    }
}
