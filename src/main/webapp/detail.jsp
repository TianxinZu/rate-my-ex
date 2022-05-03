<%@ page import="java.util.*" %>
<%@ page import="Util.*" %>
<%@ page import="java.sql.*" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="detail.css" rel="stylesheet" type="text/css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
    <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Bangers">
    <title>Detail</title>
    
</head>

<body>
<!-- TODO -->
	<%
		String username = "";
		Cookie cookies[] = request.getCookies();
	    if (cookies != null) {
	    	for (Cookie cookie : cookies) {
	    		if (cookie.getName().equals("username")) username = cookie.getValue().replace("=", " "); 
	    	}
	    }
	    
	    String found = request.getParameter("personId");
	    Person myPerson = new Person(1, "1", "1", 1.0, 1);
	    for(int i = 0; i < Util.Helper.myList.size(); i++){
	    	if(Helper.myList.get(i).personid.toString().equals(found)){
	    		myPerson = Util.Helper.myList.get(i);
	    	}
	    }
	    
	    try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
	    
		String mysql = "SELECT description FROM Post WHERE personid = ?";
		List<String> comments = new ArrayList<String>();
		
		try (Connection conn = DriverManager.getConnection(Util.Constant.Url, Util.Constant.DBUserName, Util.Constant.DBPassword);
	      	       PreparedStatement ps = conn.prepareStatement(mysql);){
			
			ps.setInt(1, myPerson.personid);
			
			ResultSet myresult = ps.executeQuery();
			
			
			while(myresult.next()) {
				comments.add(myresult.getString(1));
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		session = request.getSession(); 
		session.setAttribute("personid", myPerson.personid);
	%>
    <div id="navbar">
        <div id="navbar-left">
            <a href="index.jsp" id="logo">Rate my ex</a>
            <%
            	if (!username.equals("")) {
            		out.print("<span id=\"welcome\">Hi, "+ username + "!</span>");
            	}
            %>
        </div>
        <div id="navbar-right">
            <a href="index.jsp" class="navbar-link">Home</a>
            <%
            	if (!username.equals("")) {
            		%><a href="LogoutDispatcher" class="navbar-link">logout</a><%
            	}
            	else {
            		%><a href="login.jsp" class="navbar-link">login/register</a><%
            	}
            %>
        </div>
      </div>
	
</div>

	    <div id="search">
        <div id="search-text">
            <p>Enter a name to see the comments</p>
        </div>
        <div id="search-box">
            <form action="SearchServlet" method="GET">
                <div id="search-text-box">
                    <input type="text" name = "text-box" id="text-box" placeholder="Name">
                </div>
            </form>
        </div>
    </div>
	
	<div id="all-details">
	    <h1><%=myPerson.name%></h1>
	    <div id="details">
	        <div id="text">
	            <p>Gender: 
	            <%= myPerson.gender %></p >
	            <p>Review Count:
				<%= myPerson.rating_count%></p >
	            <p>Rating: 
	             <% double rating = myPerson.overall_rating;
             		for (int i=0; i < (int)rating; i++){%>
              			<span class="fa fa-star checked"></span>
             		<% } %>
             		<% if(rating - (int)rating > 0) {%>
              			<i class="fa fa-star-half-o"></i>
             		<% } %></p >
             	<p>Comments: 
	             <% 
             		for (int i=0; i < comments.size(); i++){%>
              			<p><%= comments.get(i) %></p>
             		<% } %>
        	</div>
    	</div>
	</div>
	
	<div id="add">
		<form action="AddDispatcher" method="GET">
    		<h1 class="add-header">Add a comment</h1>
    		<p>Comment</p>
    		<textarea id="comment" name="comment" rows="4" cols="50"></textarea>
    		<br/><br/>
    		<p>Rating from 0 to 10</p>
    		<input type="range" id="rating" name="rating"
         	min="0" max="10">
    		<br/><br/>
    		<button type="submit" name="submit" class="regbutton"><i class="fa fa-sign-in"></i>    Submit</button>
    	</form>
	</div>
</body>
</html>