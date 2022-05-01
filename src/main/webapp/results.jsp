<%@ page import="Util.*" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.sql.*" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1" %>
<%@page import="java.sql.DriverManager"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.PreparedStatement"%>
<%@page import="java.sql.Connection"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Search</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link
            href="https://fonts.googleapis.com/css2?family=Lobster&family=Roboto:wght@300&display=swap"
            rel="stylesheet">
    <link rel="stylesheet" href="index.css">
    <script src="https://kit.fontawesome.com/3204349982.js"
            crossorigin="anonymous"></script>
	<script src="https://apis.google.com/js/platform.js" async defer></script>
    <meta name="google-signin-client_id" content="49497119558-qa34tucl46nbq34fmbeipto8s1a2scup.apps.googleusercontent.com">
    
    <%
        //TODO perform search
        // search through the database and find the matching restaurant
        // search through data base and find according to category its corresponding name
        
		
	    String searchText = request.getParameter("text-box");
    	searchText = (String)session.getAttribute("searchText");
    	
    	System.out.println(searchText);
		
		String username = "";
		Cookie cookies[] = request.getCookies();
	    if (cookies != null) {
	    	for (Cookie cookie : cookies) {
	    		if (cookie.getName().equals("username")) username = cookie.getValue().replace("=", " "); 
	    	}
	    }
		
    %>
</head>
<body>
	<!-- TODO -->
	<div class="g-signin2" style="display: none;"></div>
     <br><br><br>
      <p id="name"></p>
      <div id="status">
   </div>
   <script type="text/javascript">
      function onSignIn(googleUser) {
      
	      var profile = googleUser.getBasicProfile();
	      var imagurl=profile.getImageUrl();
	      var name=profile.getName();
	      var email=profile.getEmail();
      }  
	</script>
	
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

	    <div id="search">
        <div id="search-text">
            <p>Enter a name to see the comments</p>
        </div>
        <div id="search-box">
            <form action="SearchServlet" method="GET">
                <div id="search-text-box">
                    <input type="text" name="text-box" id="text-box" placeholder="Name">
                </div>
            </form>
        </div>
    </div>
	
	<div id="search-main">
	    <h1>Results for <%= searchText %></h1>
	    <hr>
	    <!--  loop through all of the search results  -->
	    <%    
	    for(int num = 0; num < Helper.myList.size(); num++) {
		      Person temp = Helper.myList.get(num);
		      Integer id = temp.personid;
		%>
		      <div class="section">
		         <div class="name">
		             <h3><%=temp.name%></h3>
		         </div>
		         <div class="texts">
		             <a href="detail.jsp?personId=<%= id %>">
		             View details
		             </a>
		             
		             <p>Gender: <%= temp.gender %></p >
		             <p>Count: <%= temp.rating_count %></p >
		             <p>Rating: <%  double rating = temp.overall_rating;
		             for (int i = 0; i < (int)rating; i++) { %>
		             	<span class="fa fa-star checked"></span>
		             <%}%>
		             <%if(rating - (int)rating > 0){%>
		             	<i class="fa fa-star-half-o"></i>
		             <%} %></p >
		          <br>
		      
		         </div>
		     </div>
		     <hr>
	     <% } %>
	</div>

</body>
</html>