import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serial;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import Util.*; 
/**
 * Servlet implementation class SearchDispatcher
 */
@WebServlet("/SearchDispatcher")
public class SearchDispatcher extends HttpServlet {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Default constructor.
     */
    public SearchDispatcher() {
    	
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        ServletContext servletContext = getServletContext();
    }
    

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO
    	response.setContentType("text/html");
		String searchText = request.getParameter("searchText");
		String searchBy = request.getParameter("searchBy");
		String orderBy = request.getParameter("orderBy");
		
		if(searchBy.equals("1")) {
			searchBy = "name";
		}
		else {
			searchBy = "overall_rating";
		}
		
		try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
		
		String mysql = "SELECT * FROM Person WHERE ? LIKE ? ORDER BY ? DESC";
		
		try (Connection conn = DriverManager.getConnection(Util.Constant.Url, Util.Constant.DBUserName, Util.Constant.DBPassword);
	      	       PreparedStatement ps = conn.prepareStatement(mysql);){
			
			ps.setString(1, searchBy);
			ps.setString(2, "%"+searchText+"%");
			ps.setString(3, orderBy);
			
			ResultSet myresult = ps.executeQuery();
			
			Util.Helper.myList.clear();
			
			while(myresult.next()) {
				Person temp = new Person(myresult.getInt(1), myresult.getString(2), myresult.getString(3), myresult.getDouble(4), myresult.getInt(5));
				Util.Helper.myList.add(temp);
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		// store search criterias, all should be string
		HttpSession session = request.getSession(); // if we need to getSession or not
		session.setAttribute("searchText", searchText);
		session.setAttribute("searchBy", searchBy);
		session.setAttribute("orderBy", orderBy);
		
		request.getRequestDispatcher("/results.jsp").forward(request, response);
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO Auto-generated method stub
        doGet(request, response);
    }
}