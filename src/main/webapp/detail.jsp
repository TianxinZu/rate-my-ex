<%@ page import="java.util.*" %>
<%@ page import="Util.*" %>
<%@ page import="java.sql.*" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Details</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link
            href="https://fonts.googleapis.com/css2?family=Lobster&family=Roboto:wght@300&display=swap"
            rel="stylesheet">
    <script src="https://kit.fontawesome.com/3204349982.js"
            crossorigin="anonymous"></script>
    <link rel="stylesheet" href="index.css">
    
    <script src="https://apis.google.com/js/platform.js" async defer></script>
    <meta name="google-signin-client_id" content="49497119558-qa34tucl46nbq34fmbeipto8s1a2scup.apps.googleusercontent.com">
    
    
</head>



<body>
<!-- TODO -->

<div class="g-signin2" data-onsuccess="onSignIn" id="myP" style="display: none;"></div>
      <br><br><br><br>
      <p id="name"></p>
      <div id="status">
   </div>
   <script type="text/javascript">
      function onSignIn(googleUser) {
      
      var profile = googleUser.getBasicProfile();
      var imagurl=profile.getImageUrl();
      var name=profile.getName();
      var email=profile.getEmail();
      //document.getElementById("myImg").src = imagurl;
      //document.getElementById("name").innerHTML = name;
      //document.getElementById("myP").style.visibility = "hidden";
      //document.getElementById("status").innerHTML = 'Welcome '+name+'!<a href=success.jsp?email='+email+'&name='+name+'/>Continue with Google login</a></p>';
      //window.location.href='success.jsp?email='+email+'&name=' + name;
   }
   </script>

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
	%>
    <div id="navbar">
        <div id="navbar-left">
            <a href="index.jsp" id="logo">Rate my ex</a>
            <%
            	if (!username.equals("")) {
            		out.print("Hi "+ username + "!");
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
</body>
</html>