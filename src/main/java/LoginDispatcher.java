import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.*;
import java.sql.*;
import java.util.regex.Pattern;
import org.apache.ibatis.jdbc.ScriptRunner;

import javax.servlet.http.Cookie;

/**
 * Servlet implementation class LoginDispatcher
 */
@WebServlet("/LoginDispatcher")
public class LoginDispatcher extends HttpServlet {
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
   
    	String email = request.getParameter("email");
    	String password = request.getParameter("password");
    	String error = "";
    	String name = "";
    	Integer userid = 0;
    	
    	if (email == null || email.equals("")) error += "<p>Missing email address. ";
    	else if (!Pattern.matches(Util.Constant.emailPattern, email)) error += "<p>Invalid email address. ";
    	else if (password == null || password.equals("")) error += "<p>Missing password. ";

    	if (!error.equals(""))
    	{
    		error += "Please enter again.</p>";
    		request.setAttribute("error", error);
			request.getRequestDispatcher("login.jsp").include(request, response);
			return;
    	}
    	
    	try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    	
    	try {
    		//Registering the Driver
//	    	DriverManager.registerDriver(new com.mysql.jdbc.Driver());
	        //Getting the connection
	        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306", Util.Constant.DBUserName, Util.Constant.DBPassword);
	        //Initialize the script runner
	        ScriptRunner sr = new ScriptRunner(con);
	        //Creating a reader object
	        ServletContext servletContext = getServletContext();
	        InputStream is = servletContext.getResourceAsStream("rate-my-ex.sql");
	        Reader reader = new InputStreamReader(is);
	        //Running the script
	        sr.runScript(reader);
	        
    	}
    	catch (SQLException e) {System.out.println(e.getMessage());}
    	
    	String check = "SELECT username, userpassword, userid FROM Username WHERE email = ?";
    	
    	try (Connection conn = DriverManager.getConnection(Util.Constant.Url, Util.Constant.DBUserName, Util.Constant.DBPassword);
      	       PreparedStatement ps = conn.prepareStatement(check);) 
        {
         	ps.setString(1, email);
         	ResultSet rs = ps.executeQuery();
         	if (rs.next()) {
         		name = rs.getString("username");
         		userid = rs.getInt("userid");
         		if (!password.equals(rs.getString("userpassword"))) 
         		{
         			error = "<p>Password doesn't match. Please enter again.</p>";
             		request.setAttribute("error", error);
        			request.getRequestDispatcher("login.jsp").include(request, response);
//        			System.out.println("wrong password");
        			return;
         		}
         	}
         	else {
         		error = "<p>Email not found. Please register.</p>";
         		request.setAttribute("error", error);
    			request.getRequestDispatcher("login.jsp").include(request, response);
    			return;
         	}
         	
        }
        catch (SQLException ex) {System.out.println("SQLException: " + ex.getMessage());}
    	
    	Cookie cookie = new Cookie("username", name);
    	cookie.setMaxAge(3600);
    	response.addCookie(cookie);
    	
    	String userID = userid.toString();
    	Cookie mycookie = new Cookie("userid", userID);
    	response.addCookie(mycookie);
    	//request.getRequestDispatcher("index.jsp").forward(request, response);
    	response.sendRedirect("index.jsp");
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