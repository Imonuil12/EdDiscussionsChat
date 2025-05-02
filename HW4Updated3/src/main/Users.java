package main;
/*******
 * <p> Title: Reviews Class. </p>
 * 
 * <p> Description: Class that has functions to return lists of certain users. </p>
 * @author Jacqui Person
 * 
 */
import exception.ValidationException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Users {
	
  /**
    * Returns a list of all the admins' usernames in the system.
    * 
	* @return usernames the list of all the usernames
	* 
	*/
	public List<String> getAllAdmins()
	{
		List<String> usernames = new ArrayList<>();
		try (Connection conn = DatabaseManager.getConnection();
				PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE role = ?")
		        )	{
			stmt.setString(1, "Admin");
			ResultSet rs = stmt.executeQuery();
			 while (rs.next()) {
	                String username = rs.getString("username");
	                usernames.add(username);
			 }
		}
		catch (SQLException e) {
            e.printStackTrace();
        }
		return usernames;
	}
	
	 /**
	    * Returns a list of all the reviewers' usernames in the system.
	    * 
		* @return usernames the list of all the usernames
		* 
		*/
		public List<String> getAllReviewers()
		{
			List<String> usernames = new ArrayList<>();
			try (Connection conn = DatabaseManager.getConnection();
					PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE role = ?")
			        )	{
				stmt.setString(1, "Reviewer");
				ResultSet rs = stmt.executeQuery();
				 while (rs.next()) {
		                String username = rs.getString("username");
		                usernames.add(username);
				 }
			}
			catch (SQLException e) {
	            e.printStackTrace();
	        }
			return usernames;
		}
	
  /**
	* Returns a list of all the instructors' usernames in the system.
	* 
    * @return usernames the list of all the usernames
	* 
	*/
	public List<String> getAllInstructors()
	{
		List<String> usernames = new ArrayList<>();
		try (Connection conn = DatabaseManager.getConnection();
				PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE role = ?")
		        )	{
			stmt.setString(1, "Instructor");
			ResultSet rs = stmt.executeQuery();
			 while (rs.next()) {
	                String username = rs.getString("username");
	                usernames.add(username);
			 }
		}
		catch (SQLException e) {
            e.printStackTrace();
        }
		return usernames;
	}

}
