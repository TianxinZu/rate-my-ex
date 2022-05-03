import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.jdbc.ScriptRunner;

import Util.Constant;

import java.io.*;
import java.sql.*;
import java.util.regex.Pattern;

/**
 * Servlet implementation class RegisterDispatcher
 */
@WebServlet("/RegisterDispatcher")
public class RegisterDispatcher extends HttpServlet {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Default constructor.
     */
    public RegisterDispatcher() {
    }


    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     * response)
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	response.setContentType("text/html");
        String email = request.getParameter("email");
        String name = request.getParameter("name");
        String password = request.getParameter("password");
        String confirmedPassword = request.getParameter("confirmed_password");
        String[] terms = request.getParameterValues("terms");
        String error = "";
        Integer userid = 0;
        
        if (email == null || email.equals("")) error += "<p>Missing email address. ";
        else if (!Pattern.matches(Util.Constant.emailPattern, email)) error += "<p>Invalid email address. ";
        else if (name == null || name.equals("")) error += "<p>Missing username. ";
        else if (!Pattern.matches(Util.Constant.namePattern, name)) error += "<p>Invalid username. ";
        else if (password == null || password.equals("")) error += "<p>Missing password. ";
        else if (confirmedPassword == null || confirmedPassword.equals("")) error += "<p>Please confirm your password. ";
        else if (!password.equals(confirmedPassword)) error += "<p>Passwords do not match. ";
        else if (terms == null || terms.length == 0) error += "<p>Please check the term box. ";
        
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
        
        String check = "SELECT email FROM Username WHERE email = ?";
        String insert = "INSERT INTO Username (email, username, userpassword) VALUES (?, ?, ?)";
        
        try (Connection conn = DriverManager.getConnection(Util.Constant.Url, Util.Constant.DBUserName, Util.Constant.DBPassword);
     	       PreparedStatement ps = conn.prepareStatement(check);) 
        {
        	ps.setString(1, email);
        	ResultSet rs = ps.executeQuery();
        	if (rs.next()) 
        	{
        		error = "<p>Email was already registered. Please log in.</p>";
        		request.setAttribute("error", error);
    			request.getRequestDispatcher("login.jsp").include(request, response);
    			return;
        	}
        	
        }
        catch (SQLException ex) {System.out.println("SQLException: " + ex.getMessage());}
        
        try (Connection conn = DriverManager.getConnection(Util.Constant.Url, Util.Constant.DBUserName, Util.Constant.DBPassword);
        	       PreparedStatement ps = conn.prepareStatement(insert);) 
        {
        	ps.setString(1, email);
        	ps.setString(2, name);
        	ps.setString(3, password);
        	ps.executeUpdate();
        } 
        catch (SQLException ex) 
        {
        	System.out.println ("SQLException: " + ex.getMessage());
        }
        
        String getID = "SELECT userid FROM Username WHERE email = ?";
        try (Connection conn = DriverManager.getConnection(Util.Constant.Url, Util.Constant.DBUserName, Util.Constant.DBPassword);
     	       PreparedStatement ps = conn.prepareStatement(getID);) 
        {
        	ps.setString(1, email);
        	ResultSet rs = ps.executeQuery();
        	rs.next();
        	userid = rs.getInt("userid");
        } 
        catch (SQLException ex) 
        {
        	System.out.println ("SQLException: " + ex.getMessage());
        }
        
        Cookie cookie = new Cookie("username",name);
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
