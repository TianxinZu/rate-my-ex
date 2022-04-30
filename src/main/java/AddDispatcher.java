import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/AddDispatcher")
public class AddDispatcher extends HttpServlet{
	public AddDispatcher() {}
	
	@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
		response.setContentType("text/html");
		String des=request.getParameter("description");
		
//		String error = "";
//		if (des == null || des.equals("")) error += "<p>Missing description. ";
//		
//		if (!error.equals("")) 
//        {
//        	error += "Please enter again.</p>";
//        	request.setAttribute("error", error);
//			request.getRequestDispatcher("detail.jsp").include(request, response);
//			return;
//        }
		
		try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
		String insert ="INSERT INTO BIAO2 VALUES (?,?)";
		
		int personID=999999;
		
		try (Connection conn = DriverManager.getConnection(Util.Constant.Url, Util.Constant.DBUserName, Util.Constant.DBPassword);
	     	       PreparedStatement ps = conn.prepareStatement(insert);) 
	        {
	        	ps.setString(1, des);
	        	ps.setInt(2, personID);
	        	ResultSet rs = ps.executeQuery();
	        	
	        }
	        catch (SQLException ex) {System.out.println("SQLException: " + ex.getMessage());}
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		 doGet(request, response);
	}
	
}