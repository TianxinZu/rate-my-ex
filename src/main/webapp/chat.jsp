<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="index.css" rel="stylesheet" type="text/css">
    <link href="chat.css" rel="stylesheet" type="text/css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
    <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Bangers">
    <title>RATE MY EX</title>
    <script src="https://cdn.bootcdn.net/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
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
	    String ID = request.getParameter("userID");
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
    <div id="chat-name">
    	<p></p><%=ID%>
    </div>
    <div id="chat-history">
    	<!-- insert chat -->
    	<div id="my-chat">
    		<p>Hi! Nice to meet you! Good Morning! How's it going? What's up?</p>
    	</div>
    	<div id="your-chat">
    		<p>Hello! How are you doing? Good Evening! It's great to see you! Long-time no see!</p>
    	</div>
    </div>
    <script>
    	setInterval("getMessage()",1000);
    	function getMessage(){
    		$.ajax({
                url: ,
                type: ,
                data: ,
                success:function (args) {
                	
                }
            })
    	}
    </script>
    <div id="chat">
    	<form action="ChatDispatcher" method="GET">
    		<div id="chat-text-box">
    			<input type="text" name="text" id="chat-box" placeholder="Type something ...">
    			<input type="hidden" name="otherUserID" value=<%=ID%>>
    		</div>
    	</form>
    </div>
</body>
</html> 