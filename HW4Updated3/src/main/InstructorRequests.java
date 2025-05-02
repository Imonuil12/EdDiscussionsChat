package main;
/*******
 * <p> Title: InstructorRequests Class. </p>
 * 
 * <p> Description: Class that manages instructor requests, including
 * methods to create them and get lists of them. </p> 
 * @author Jacqui Person
 * 
 */

import java.sql.*;

import exception.ValidationException;
import java.util.ArrayList;
import java.util.List;


public class InstructorRequests {
	
   /**
	 * Creates a new InstructorRequest and saves it to the database.
	 *
	 * @param instructorUsername the username of the instructor who made the request
	 * @param adminUsername the username of the admin who is being requested to do something
	 * @param status the status of the request
	 * @param text the text of request
	 */
	 public void createInstructorRequest(String instructorUsername, String adminUsername, String text, String status) throws ValidationException {
	        try (Connection conn = DatabaseManager.getConnection();
	             PreparedStatement stmt = conn.prepareStatement(
	                 "INSERT INTO instructors_requests (instructor_username, admin_username, text, status) VALUES (?, ?, ?, ?)")
	        ) {
	            stmt.setString(1, instructorUsername);
	            stmt.setString(2, adminUsername);
	            stmt.setString(3, text);
	            stmt.setString(4, status);
	            stmt.executeUpdate();
	            
	            System.out.print("Created an instructor request with instructor username: " + instructorUsername + ", admin username: " + adminUsername + 
	            		", text: " + text + ", status: " + status);
	        } 
	        catch (SQLException e) {
	            throw new ValidationException("Failed to save instructor request " + e.getMessage());
	        }
	    }
	 
   /**
	 * Returns a list of all the InstructorRequests in the database.
	 * 
	 * @return iRequests the list of all requests
	 * 
	 */
	 public List<InstructorRequest> getAllInstructorRequests() {
	        List<InstructorRequest> iRequests = new ArrayList<>();
	        try (Connection conn = DatabaseManager.getConnection();
	        	 Statement stmt = conn.createStatement();
	            ResultSet rs = stmt.executeQuery("SELECT * FROM instructors_requests"))
	         {
	            while (rs.next()) {
	            	String fromInstructor = rs.getString("instructor_username");
	            	String toAdmin = rs.getString("admin_username");
	            	String text = rs.getString("text");
	            	String status = rs.getString("status");
	            	
	            	InstructorRequest iR = new InstructorRequest(fromInstructor, toAdmin, text, status);
	            	iRequests.add(iR);
	               
	            }
	        } 
	        catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return iRequests;
	    }
	 
   /**
	 * Returns a list of all the InstructorRequests for a specific admin.
	 * 
	 * @return iRequests the list of the requests
	 * 
	 */
	 public List<InstructorRequest> getInstructorRequests(String toAdmin) throws ValidationException
	 {
		  List<InstructorRequest> iRequests = new ArrayList<>();
	        try (Connection conn = DatabaseManager.getConnection();
	        	 PreparedStatement stmt = conn.prepareStatement("SELECT * FROM instructors_requests WHERE admin_username = ?"))
	         {
	        	stmt.setString(1, toAdmin);
	        	ResultSet rs = stmt.executeQuery();
	            while (rs.next()) 
	            {
	            	String from_instructor = rs.getString("instructor_username");
	            	String to_admin = rs.getString("admin_username");
	            	String text = rs.getString("text");
	            	String status = rs.getString("status");
	            	
	            	InstructorRequest iR = new InstructorRequest(from_instructor, to_admin, text, status);
	            	iRequests.add(iR);
	           }
	        } 
	        catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return iRequests;
	 }
	 
  /**
    * Updates the completion status of an InstructorRequest.
	* 
    * @param fromInstructor the instructor who made the request
    * @param toAdmin the admin who the request is for
    * @param the new status of the request
	* 
	*/
	 public static boolean updateInstructorRequestStatus(String fromInstructor, String toAdmin, String status, String text)
	 {
		  try (Connection conn = DatabaseManager.getConnection();
		             PreparedStatement stmt = conn.prepareStatement("UPDATE instructors_requests SET status = ? WHERE instructor_username = ? AND admin_username = ? AND text = ?")
		        ) {
		            stmt.setString(1, status);
		            stmt.setString(2, fromInstructor);
		            stmt.setString(3, toAdmin);
		            stmt.setString(4, text);
		            stmt.executeUpdate();
		        } 
		        catch (SQLException e) {
		        	e.printStackTrace();
		        	return false;
		        }
		  System.out.print("Instructor request status successfully updated to: " + status);
		        return true;
	 }
	 
  /**
	* Returns a list of all the completed InstructorRequests in the system.
	* 
	* @return requests the list of completed requests
    * 
    */
	 public static List<InstructorRequest> getCompletedInstructorRequests() throws SQLException
	 {
		 List<InstructorRequest> requests = new ArrayList<>();
		  try (Connection conn = DatabaseManager.getConnection();
		             PreparedStatement stmt = conn.prepareStatement("SELECT * from instructors_requests WHERE status = ?")
		        ) {
			  stmt.setString(1, "complete");
			  ResultSet rs = stmt.executeQuery();
	            while (rs.next()) 
	            {
	            	String from_instructor = rs.getString("instructor_username");
	            	String to_admin = rs.getString("admin_username");
	            	String text = rs.getString("text");
	            	String status = rs.getString("status");
	            	
	            	InstructorRequest iR = new InstructorRequest(from_instructor, to_admin, text, status);
	            	requests.add(iR);
	            }
	            return requests;
		  }
	 }

}
