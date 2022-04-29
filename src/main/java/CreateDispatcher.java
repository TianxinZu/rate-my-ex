import java.io.IOException;
import java.io.Serial;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/WriteDispatcher")
public class CreateDispatcher extends HttpServlet{
	public CreateDispatcher(){}
	@Override
	 protected void doGet(HttpServletRequest request, HttpServletResponse response)
	            throws ServletException, IOException {
		response.setContentType("text/html");
		String name= request.getParameter("name");
		String birth=  request.getParameter("birth");
		String rating =request.getParameter("rating");
		String description=request.getParameter("description");
		
		
		String error = "";
		if (name == null || name.equals("")) error += "<p>Missing name. ";
        else if (birth == null || birth.equals("")) error += "<p>Missing birth. ";
        else if (rating == null || rating.equals("")) error += "<p>Missing rating. ";
        else if (description == null || description.equals("")) error += "<p>Missing description. ";
		
		if (!error.equals("")) {
	        error += "Please enter again.</p>";
	       	request.setAttribute("error", error);
			request.getRequestDispatcher("auth.jsp").include(request, response);
			return;
		}
		
		try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        
	 }
	 
	 @Override
	 protected void doPost(HttpServletRequest request, HttpServletResponse response)
	            throws ServletException, IOException {
		 doGet(request, response);
	 }
}