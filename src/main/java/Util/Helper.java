package Util;

import java.util.List;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Helper {
	public static List<Person> myList;
	
	public Helper() {
		
	}
	
	public void recalculate(Integer personID, Integer rating) {
    	try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    	
    	String check = "SELECT * FROM Post WHERE personid = ?";
    	Integer count = 0;
    	
    	try (Connection conn = DriverManager.getConnection(Util.Constant.Url, Util.Constant.DBUserName, Util.Constant.DBPassword);
       	       PreparedStatement ps = conn.prepareStatement(check);) 
         {
          	ps.setInt(1, personID);
          	ResultSet rs = ps.executeQuery();
          	while(rs.next()) {
          		count++;
          	}
         }
    	 catch (SQLException ex) {System.out.println("SQLException: " + ex.getMessage());}
    	
    	String search = "SELECT * FROM Person WHERE personid = ?";
    	Double overRating = 0.;
    	try (Connection conn = DriverManager.getConnection(Util.Constant.Url, Util.Constant.DBUserName, Util.Constant.DBPassword);
        	       PreparedStatement ps = conn.prepareStatement(search);) 
          {
           	ps.setInt(1, personID);
           	ResultSet rs = ps.executeQuery();
           	rs.next();
           	overRating = rs.getDouble("overall_rating");
          }
     	 catch (SQLException ex) {System.out.println("SQLException: " + ex.getMessage());}
    	
    	Double newRating = (overRating * count + rating)/(count+1);
    	
    	String update = "UPDATE Person SET overall_rating=?, rating_count=? WHERE personid=?";
    	try (Connection conn = DriverManager.getConnection(Util.Constant.Url, Util.Constant.DBUserName, Util.Constant.DBPassword);
     	       PreparedStatement ps = conn.prepareStatement(update);) 
        {
        	ps.setDouble(1, newRating);
        	ps.setInt(2, count+1);
        	ps.setInt(3, personID);
        	ps.execute();
        }
        catch (SQLException ex) {System.out.println("SQLException: " + ex.getMessage());}
	}
}
