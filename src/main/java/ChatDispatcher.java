import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.*;
import java.sql.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import Util.Constant;
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
    	int userID = -1;
    	Cookie[] cookies = request.getCookies();
    	if (cookies != null)
    	{
    		for (Cookie cookie: cookies)
    		{
    			if (cookie.getName().equals("userid")) userID = Integer.valueOf(cookie.getValue());
    		}
    	}
//    	Map<String, ArrayList<Message>> name_to_messages = getMessages(userID, userid_to_username);
//    	for (Map.Entry<String, ArrayList<Message>> entry : name_to_messages.entrySet())
//    	{
//    		System.out.println(entry.getValue());
//    	}
    	response.setContentType("text/html");
    	String text = request.getParameter("text");
    	int otherUserID = Integer.valueOf(request.getParameter("otherUserID"));
    	Timestamp createdTime = new Timestamp(System.currentTimeMillis());
    	if (text != null && !text.isEmpty())
    	{
	    	Message message = new Message(text, userID, otherUserID, createdTime);
	    	message.insertIntoDatabase();
    	}
//    	System.out.println(timestamp.getClass().getName());
//    	System.out.println(Util.Constant.dateFormat.format(createdTime));
    	request.setAttribute("messages", name_to_messages.keySet());
    	response.sendRedirect("chat.jsp");
    }
    
    public ArrayList<Message> getMessages(int userID, int otherUserID)
    {
    	ArrayList<Message> messages = new ArrayList<Message>();
    	try
    	{
    		Class.forName("com.mysql.jdbc.Driver");
			Connection connection = DriverManager.getConnection(Constant.Url, Constant.DBUserName, Constant.DBPassword);
			String sql = "SELECT * FROM Message WHERE senderID = ? OR receiverID = ?";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setInt(1, userID);
			ps.setInt(2, userID);
			ResultSet rs = ps.executeQuery();
			while (rs.next())
			{
				String text = rs.getString("text");
				int senderID = rs.getInt("senderID");
				int receiverID = rs.getInt("receiverID");
				Timestamp createdTime = rs.getTimestamp("createdTime");
				Message message = new Message(text, senderID, receiverID, createdTime);
				int otherUserID = senderID == userID ? receiverID : senderID;
				String otherUserName = userid_to_username.get(otherUserID);
				if (otherUserName != null)
				{
					if (!name_to_messages.containsKey(otherUserName)) name_to_messages.put(otherUserName, new ArrayList<Message>());
					name_to_messages.get(otherUserName).add(message);
				}
			}
			ps.close();
    	}
    	catch (Exception e) {e.printStackTrace();}
    	return name_to_messages;
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