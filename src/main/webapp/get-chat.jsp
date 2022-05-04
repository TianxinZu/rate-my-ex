<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
</head>
<body>
	<%@ page import ="java.util.ArrayList"%>
	<%@ page import ="java.sql.*"%>
	<%@ page import ="Util.Constant"%>
	<%@ page import ="Util.Message"%>
	<%
		int userID = -1;
		Cookie[] cookies = request.getCookies();
		if (cookies != null)
		{
			for (Cookie cookie: cookies)
			{
				if (cookie.getName().equals("userid")) userID = Integer.valueOf(cookie.getValue());
			}
		}
		String otherUserName = request.getParameter("otherUserName");
		String otherUserIDString = request.getParameter("otherUserID");
		int otherUserID = -1;
		if (otherUserIDString == null)
		{
			try
	    	{
	    		Class.forName("com.mysql.jdbc.Driver");
				Connection connection = DriverManager.getConnection(Constant.Url, Constant.DBUserName, Constant.DBPassword);
				String sql = "SELECT userid FROM Username WHERE username = ?";
				PreparedStatement ps = connection.prepareStatement(sql);
				ps.setString(1, otherUserName);
				ResultSet rs = ps.executeQuery();
				if (rs.next())
				{
					otherUserID = rs.getInt("userid");
				}
				ps.close();
	    	}
	    	catch (Exception e) {e.printStackTrace();}
		}
		else otherUserID = Integer.valueOf(otherUserIDString);
		ArrayList<ArrayList<Object>> messages = new ArrayList<ArrayList<Object>>();
    	try
    	{
    		Class.forName("com.mysql.jdbc.Driver");
			Connection connection = DriverManager.getConnection(Constant.Url, Constant.DBUserName, Constant.DBPassword);
			String sql = "SELECT * FROM Message WHERE senderID = ? AND receiverID = ? OR senderID = ? AND receiverID = ? ORDER BY createdTime";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setInt(1, userID);
			ps.setInt(2, otherUserID);
			ps.setInt(3, otherUserID);
			ps.setInt(4, userID);
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
    	String output = "";
    	for (int i = 0; i < messages.size(); i++)
    	{
    		ArrayList<Object> message = messages.get(i);
    		output += "<div id='time'><p>" + message.get(1) + "</p></div>";
    		if ((boolean) message.get(2)) output += "<div id='my-chat'><p>" + message.get(0) + "</p></div>";
    		else
    		{
    			output += "<div id='your-chat'><p>" + message.get(0) + "</p></div>";
    		}
    	}
    	request.setAttribute("otherUserName", otherUserName);
    	request.setAttribute("otherUserID", otherUserID);
    	out.println(output);
	%>
</body>
</html>