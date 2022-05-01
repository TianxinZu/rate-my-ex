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
import java.util.ArrayList;

import Util.*; 
/**
 * Servlet implementation class SearchDispatcher
 */
@WebServlet("/SearchServlet")
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
		String searchText = request.getParameter("text-box");
		
		System.out.println(searchText);
		
		
		try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
		
		String mysql = "SELECT * FROM Person WHERE name LIKE ?";
		
		try (Connection conn = DriverManager.getConnection(Util.Constant.Url, Util.Constant.DBUserName, Util.Constant.DBPassword);
	      	       PreparedStatement ps = conn.prepareStatement(mysql);){
			
			ps.setString(2, "%"+searchText+"%");
			
			ResultSet myresult = ps.executeQuery();
			
			Util.Helper.myList = new ArrayList<Person>();
			
			if(Util.Helper.myList == null) {
				System.out.println("NULL");
				System.exit(0);
			}
			
			while(myresult.next()) {
				Person temp = new Person(myresult.getInt(1), myresult.getString(2), myresult.getString(3), myresult.getDouble(4), myresult.getInt(5));
				Util.Helper.myList.add(temp);
				
			}
			
			
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		//Util.Helper.myList = new ArrayList<Person>();
		//Person test = new Person(1, "1", "1", 1.0, 1);
		//Util.Helper.myList.add(test);
		//System.out.println(Util.Helper.myList.size());
		
		// store search criterias, all should be string
		HttpSession session = request.getSession(); // if we need to getSession or not
		session.setAttribute("searchText", searchText);
		
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