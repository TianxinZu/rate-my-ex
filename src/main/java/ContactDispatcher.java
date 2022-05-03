import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.*;
import java.sql.*;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;
import org.apache.ibatis.jdbc.ScriptRunner;

import Util.Message;

import javax.servlet.http.Cookie;

/**
 * Servlet implementation class LoginDispatcher
 */
@WebServlet("/ContactDispatcher")
public class ContactDispatcher extends HttpServlet {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     * response)
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	
    	response.setContentType("text/html");
    	Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    	Message message = new Message("Hello World", 0, 1, timestamp);
    	message.insertIntoDatabase();
//    	System.out.println(timestamp.getClass().getName());
    	System.out.println(Util.Constant.dateFormat.format(timestamp));
    	response.sendRedirect("contact.jsp");
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     * response)
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}