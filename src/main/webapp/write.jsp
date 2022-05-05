<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="index.css" rel="stylesheet" type="text/css">
    <link href="write.css" rel="stylesheet" type="text/css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
    <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Bangers">
    <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Rajdhani">
    <title>RATE MY EX</title>
</head>
<body>
	<%
		String username = "";
		Cookie cookies[] = request.getCookies();
	    if (cookies != null) {
	    	for (Cookie cookie : cookies) {
	    		if (cookie.getName().equals("username")) username = cookie.getValue().replace("=", " "); 
	    	}
	    }
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
        <div id="navbar-middle">
        	<a href="ContactDispatcher" class="navbar-link">Chat</a>
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
    <div id="add-person">
    	<h1 class="header">Add a new person</h1>
    	<form action="AddDispatcher" method="GET">
    	<div class="element">
    		<label>Enter a name: </label>
    		<div class="input">
    			<input type="text" class="user-inf">
    		</div>
    	</div>
    	<div class="element">
    		<label>Gender: </label>
    		<div class="input">
    			<label><input type="radio" name="gender" value="male" />Male</label>
    			<label><input type="radio" name="gender" value="female" />Female</label>
    			<label><input type="radio" name="gender" value="other" />Other</label>
    		</div>
    	</div>
    	<div class="element">
    		<label>Choose a rating: </label>
    		<div class="rate">
			    <input type="radio" id="star5" name="rate" value="5" />
			    <label for="star5" title="text">5</label>
			    <input type="radio" id="star4" name="rate" value="4" />
			    <label for="star4" title="text">4</label>
			    <input type="radio" id="star3" name="rate" value="3" />
			    <label for="star3" title="text">3</label>
			    <input type="radio" id="star2" name="rate" value="2" />
			    <label for="star2" title="text">2</label>
			    <input type="radio" id="star1" name="rate" value="1" />
			    <label for="star1" title="text">1</label>
			 </div>
    	</div>
    	<div class="element">
    	<label>Leave some comment: </label>
    		<div class="input">
    			<textarea name="description" row="5" column="40">Description...</textarea>
    		</div>
    	</div>
    	</form>
    </div>
</body>
</html> 