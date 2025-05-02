package main;
/*******
 * <p> Title: TrustedReviewers Class. </p>
 * 
 * <p> Description: Class that contains methods for reviewers, such as trusting reviewers, 
 * checking if a reviewer is trusted and getting the list of reviewers a student trusts. </p>
 * @author Jacqui Person
 * 
 */
import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import exception.ValidationException;

public class TrustedReviewers {
	
    /**
	* Makes a specific student trust a specific reviewer.
	*
	* @param student the specific student 
	* @param reviewer the specific reviewer
	* 
	*/
    public void trustReviewer(String student, String reviewer) {
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO trusted_reviewers (student, reviewer) VALUES (?, ?)")) {
            stmt.setString(1, student);
            stmt.setString(2, reviewer);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    /**
	* Returns whether a reviewer is trusted by a student or not.
	*
	* @param student the specific student to check reviewer for
	* @param reviewer the specific reviewer to check if trusted
	* 
	*/
    public boolean isTrusted(String student, String reviewer) {
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM trusted_reviewers WHERE student = ? AND reviewer = ?")) {
            stmt.setString(1, student);
            stmt.setString(2, reviewer);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
	* Returns the set of trusted reviewers for a student. 
	*
	* @param student the specific student to get the trusted reviewers for
	* @return trusted the set of trusted reviewers
	* 
	*/
    public Set<String> getTrustedReviewers(String student) {
        Set<String> trusted = new HashSet<>();
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT reviewer FROM trusted_reviewers WHERE student = ?")) {
            stmt.setString(1, student);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                trusted.add(rs.getString("reviewer"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return trusted;
    }
    
    /**
   	* Adds the student's username into the reviewer requests, gives them 
   	* a "pending" status and throws an exception if unsuccessful. 
   	*
   	* @param studentUsername the username of the student to add
   	* 
   	*/
    public void requestToBecomeReviewer(String studentUsername) throws ValidationException {
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO reviewer_requests (student_username, status) VALUES (?, 'pending')")
        ) {
            stmt.setString(1, studentUsername);
            stmt.executeUpdate();
        } 
        catch (SQLException e) {
            throw new ValidationException("Failed to request to become reviewer: " + e.getMessage());
        }
    }

    /**
   	* Checks whether the student is alreadt in the reviewer requests 
   	* with the status "pending". 
   	*
   	* @param studentUsername the username of the student to check for
   	* @return rs.next() && rs.getString("status").equals("pending") the result set that is true for pending status
   	* 
   	*/
    public boolean isReviewerRequestPending(String studentUsername) throws ValidationException {
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT status FROM reviewer_requests WHERE student_username = ?")
        ) {
            stmt.setString(1, studentUsername);
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getString("status").equals("pending");
        } 
        catch (SQLException e) {
            throw new ValidationException("Error checking request status: " + e.getMessage());
        }
    }
    
    /**
   	* Returns a list of all of the usernames of reviewers in the system. 
   	*
   	* @return reviewers the list of the reviewer usernames
   	* 
   	*/
    public List<String> getAllReviewerUsernames() {
        List<String> reviewers = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT username FROM users WHERE role = 'Reviewer'")) {
            
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                reviewers.add(rs.getString("username"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reviewers;
    }

}
