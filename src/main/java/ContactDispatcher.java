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
//    	Map<Integer, String> userid_to_username = getUsers();
//    	Map<String, ArrayList<Message>> name_to_messages = getMessages(userID, userid_to_username);
//    	System.out.println(name_to_messages.size());
//    	for (Map.Entry<String, ArrayList<Message>> entry : name_to_messages.entrySet())
//    	{
//    		System.out.println(entry.getValue());
//    	}
    	response.setContentType("text/html");
//    	Timestamp createdTime = new Timestamp(System.currentTimeMillis());
//    	Message message = new Message("Hello World", 1, 2, createdTime);
//    	message.insertIntoDatabase();
//    	System.out.println(timestamp.getClass().getName());
//    	System.out.println(Util.Constant.dateFormat.format(createdTime));
    	
    	request.setAttribute("contacts", getContacts(userID));
    	RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/contacts.jsp");
    	dispatcher.forward(request, response);
//    	response.sendRedirect("test.php");
    }
    
    public ArrayList<String> getContacts(int userID)
    {
    	ArrayList<String> contacts = new ArrayList<String>();
    	try
    	{
    		Class.forName("com.mysql.jdbc.Driver");
			Connection connection = DriverManager.getConnection(Constant.Url, Constant.DBUserName, Constant.DBPassword);
			String sql = "SELECT DISTINCT u.username FROM Username u INNER JOIN Message m ON u.userid = m.senderID AND m.receiverID = ? OR u.userid = m.receiverID AND m.senderID = ?";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setInt(1, userID);
			ps.setInt(2, userID);
			ResultSet rs = ps.executeQuery();
			while (rs.next())
			{
				String username = rs.getString("username");
				contacts.add(username);
			}
			ps.close();
    	}
    	catch (Exception e) {e.printStackTrace();}
    	return contacts;
    }
    
    public Map<Integer, String> getUsers()
    {
    	Map<Integer, String> userid_to_username = new HashMap<>();
    	try
    	{
    		Class.forName("com.mysql.jdbc.Driver");
			Connection connection = DriverManager.getConnection(Constant.Url, Constant.DBUserName, Constant.DBPassword);
			String sql = "SELECT userid, username FROM Username";
			PreparedStatement ps = connection.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next())
			{
				int userid = rs.getInt("userid");
				String username = rs.getString("username");
				userid_to_username.put(userid, username);
			}
			ps.close();
    	}
    	catch (Exception e) {e.printStackTrace();}
    	return userid_to_username;
    }
    
    public Map<String, ArrayList<Message>> getMessages(int userID, Map<Integer, String> userid_to_username)
    {
    	Map<String, ArrayList<Message>> name_to_messages = new HashMap<>();
    	String name = "";
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