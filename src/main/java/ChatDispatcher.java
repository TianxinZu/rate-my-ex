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
@WebServlet("/ChatDispatcher")
public class ChatDispatcher extends HttpServlet {
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
    	String otherUserName = request.getParameter("otherUserName");
    	int otherUserID = getUserID(otherUserName);
    	Timestamp createdTime = new Timestamp(System.currentTimeMillis());
    	if (text != null && !text.isEmpty())
    	{
	    	Message message = new Message(text, userID, otherUserID, createdTime);
	    	message.insertIntoDatabase();
    	}
    	ArrayList<ArrayList<Object>> messages = getMessages(userID, otherUserID);
    	request.setAttribute("otherUserName", otherUserName);
    	request.setAttribute("messages", messages);
    	RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/chat.jsp");
    	dispatcher.forward(request, response);
    }
    
    public int getUserID(String username)
    {
    	int userID = -1;
    	try
    	{
    		Class.forName("com.mysql.jdbc.Driver");
			Connection connection = DriverManager.getConnection(Constant.Url, Constant.DBUserName, Constant.DBPassword);
			String sql = "SELECT userid FROM Username WHERE username = ?";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setString(1, username);
			ResultSet rs = ps.executeQuery();
			if (rs.next())
			{
				userID = rs.getInt("userid");
			}
			ps.close();
    	}
    	catch (Exception e) {e.printStackTrace();}
    	return userID;
    }
    
    public ArrayList<ArrayList<Object>> getMessages(int userID, int otherUserID)
    {
    	ArrayList<ArrayList<Object>> messages = new ArrayList<ArrayList<Object>>();
    	try
    	{
    		Class.forName("com.mysql.jdbc.Driver");
			Connection connection = DriverManager.getConnection(Constant.Url, Constant.DBUserName, Constant.DBPassword);
			String sql = "SELECT * FROM Message WHERE (senderID = ? OR receiverID = ?) AND (senderID = ? OR receiverID = ?) ORDER BY createdTime";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setInt(1, userID);
			ps.setInt(2, userID);
			ps.setInt(3, otherUserID);
			ps.setInt(4, otherUserID);
			ResultSet rs = ps.executeQuery();
			while (rs.next())
			{
				String text = rs.getString("text");
				int senderID = rs.getInt("senderID");
				Timestamp tempCreatedTime = rs.getTimestamp("createdTime");
				String createdTime = Util.Constant.dateFormat.format(tempCreatedTime);
				Boolean send = false;
				if (senderID == userID)
				{
					send = true;
				}
				ArrayList<Object> message = new ArrayList<Object>();
				message.add(text);
				message.add(createdTime);
				message.add(send);
				messages.add(message);
			}
			ps.close();
    	}
    	catch (Exception e) {e.printStackTrace();}
    	return messages;
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