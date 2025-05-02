package main;
/*******
 * <p> Title: FeedbackContoller Class. </p>
 * 
 * <p> Description: Class that contains methods for feedback, like submitting and
 * getting feedback. <p>
 * @author Jacqui Person
 * 
 */
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FeedbackController {

	/**
	* Submits feedback for a review
	*
	* @param fromStudent the student
	* @param toReviewer the reviewer
	* @param text feedback text
	* 
	*/
    public void submitFeedback(String toReviewer, String fromStudent, String text) {
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO private_feedback (reviewer, student, feedbackText) VALUES (?, ?, ?)")
        ) {
            stmt.setString(1, toReviewer);
            stmt.setString(2, fromStudent);
            stmt.setString(3, text);
            stmt.executeUpdate();
            System.out.print("Saved feedback to database!!");
        } catch (SQLException e) {
            System.out.println(" Failed to save feedback: " + e.getMessage());
        }
    }

    /**
	* Returns list of feedback for a reviewer
	*
	* @param reviewer the reviewer
	* @return the list of feedback
	* 
	*/
    public List<PrivateFeedback> getFeedbackForReviewer(String reviewer) {
        List<PrivateFeedback> list = new ArrayList<>();

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT * FROM private_feedback WHERE reviewer = ?")
        ) {
            stmt.setString(1, reviewer);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String fromStudent = rs.getString("student");
                String toReviewer = rs.getString("reviewer");
                String feedbackText = rs.getString("feedbackText");

                list.add(new PrivateFeedback(toReviewer, fromStudent, feedbackText));
            }

        } catch (SQLException e) {
            System.out.println(" Failed to fetch feedback: " + e.getMessage());
        }

        return list;
    }
    
    /**
	* Returns all private feedback in database.
	*
	* @return the list of feedback
	* 
	*/
    public List<PrivateFeedback> getAllFeedback() {
        List<PrivateFeedback> list = new ArrayList<>();

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * from private_feedback"))
        {
        	 while (rs.next()) {
                 String toReviewer = rs.getString("reviewer");
                 String fromStudent = rs.getString("student");
                 String feedbackText = rs.getString("feedbackText");

                 PrivateFeedback p = new PrivateFeedback(toReviewer, fromStudent, feedbackText);
                 list.add(p);
             }
        }      
         catch (SQLException e) {
            System.out.println(" Failed to fetch all feedback: " + e.getMessage());
        }

        return list;
    }
}
