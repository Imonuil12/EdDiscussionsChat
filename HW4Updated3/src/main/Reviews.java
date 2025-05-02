package main;
/*******
 * <p> Title: Reviews Class. </p>
 * 
 * <p> Description: Class that represents the list of reviews, including methods to create, get 
 * and update reviews </p>
 * @author Jacqui Person
 * 
 */
import exception.ValidationException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Reviews {
	
	/**
	* Creates a new review and attempts to save it to the database. 
	*
	* @param answerId ID of the answer being reviewed
	* @param reviewer the reviewer of the answer
	* @param text the text of the review
	* @param weightage the weightage of the review
	* @param previousReviewId the ID of the previous review
	* 
	*/
    public void createReview(int answerId, String reviewer, String text, int weightage, Integer previousReviewId) throws ValidationException {
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                 "INSERT INTO reviews (answer_id, reviewer, text, weightage, previous_review_id) VALUES (?, ?, ?, ?, ?)")
        ) {
            stmt.setInt(1, answerId);
            stmt.setString(2, reviewer);
            stmt.setString(3, text);
            stmt.setInt(4, weightage);
            if (previousReviewId == null) {
                stmt.setNull(5, Types.INTEGER);
            } else {
                stmt.setInt(5, previousReviewId);
            }
            stmt.executeUpdate();
        } 
        catch (SQLException e) {
            throw new ValidationException("Failed to save review: " + e.getMessage());
        }
    }

    /**
	* Returns the ArrayList of reviews for an answer.
	*
	* @param answerId the ID of the answer to get the review ArrayList for
	* @return reviews the ArrayList of reviews for the answer
	* 
	*/
    public List<Review> getReviewsForAnswer(int answerId) {
        List<Review> reviews = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM reviews WHERE answer_id = ?")
        ) {
            stmt.setInt(1, answerId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Review r = new Review(
                    rs.getInt("id"),
                    rs.getInt("answer_id"),
                    rs.getString("reviewer"),
                    rs.getString("text"),
                    rs.getInt("weightage"),
                    rs.getInt("previous_review_id")
                );
                reviews.add(r);
            }
        } 
        catch (SQLException e) {
            e.printStackTrace();
        }
        return reviews;
    }
    
    /**
   	* Returns a list of all the reviews for a specific reviewer 
   	*
   	* @param reviewer the username of the reviewer
   	* @return reviews the list of the reviews
   	* 
   	*/
    public List<Review> getReviewsforReviewer(String reviewer) throws SQLException
    {
    	List<Review> reviews = new ArrayList<>();
    	try(Connection conn = DatabaseManager.getConnection();
    			PreparedStatement stmt = conn.prepareStatement("SELECT * FROM reviews WHERE reviewer = ?")
       ){
    		stmt.setString(1, reviewer);
    		ResultSet rs = stmt.executeQuery();
    		while(rs.next())
    		{
    			Review r = new Review(
    			rs.getInt("id"),
    			rs.getInt("answer_id"),
    			rs.getString("reviewer"),
    			rs.getString("text"),
    			rs.getInt("weightage"),
    			rs.getInt("previous_review_id"));
    			reviews.add(r);
    					
    		}
    		
    	}
    	 catch (SQLException e) {
             e.printStackTrace();
    	 }
    	return reviews;
    }

    /**
	* Updates a review and attempts to save it to the database. 
	*
	* @param id the ID of the review to be updated
	* @param newText the new text to update the review to
	* @param weightage the weightage of the updated review
	* 
	*/
    public boolean updateReview(int id, String newText, int weightage) throws ValidationException {
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement("UPDATE reviews SET text = ?, weightage = ? WHERE id = ?")
        ) {
            stmt.setString(1, newText);
            stmt.setInt(2, weightage);
            stmt.setInt(3, id);
            stmt.executeUpdate();
        } 
        catch (SQLException e) {
            throw new ValidationException("Failed to update review: " + e.getMessage());
        }
        return true;
    }
    

    /**
	* Allows the student to up vote a review, which updates its weightage 
	*
	* @param reviewId the ID of the review
	* @param studentId the ID of the student
	* @param upvote true for up voted, false for down voted
	* 
	*/
    public void voteReview(int reviewId, String studentId, boolean upvote) {
        int voteValue = upvote ? 1 : -1;

        try (Connection conn = DatabaseManager.getConnection()) {
        	PreparedStatement checkStmt = conn.prepareStatement(
                "SELECT vote FROM review_votes WHERE review_id = ? AND student_id = ?");
            checkStmt.setInt(1, reviewId);
            checkStmt.setString(2, studentId);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                int existingVote = rs.getInt("vote");
                if (existingVote == voteValue) return; 
                
                PreparedStatement updateStmt = conn.prepareStatement(
                    "UPDATE review_votes SET vote = ? WHERE review_id = ? AND student_id = ?");
                updateStmt.setInt(1, voteValue);
                updateStmt.setInt(2, reviewId);
                updateStmt.setString(3, studentId);
                updateStmt.executeUpdate();
            } else {
                
                PreparedStatement insertStmt = conn.prepareStatement(
                    "INSERT INTO review_votes (review_id, student_id, vote) VALUES (?, ?, ?)");
                insertStmt.setInt(1, reviewId);
                insertStmt.setString(2, studentId);
                insertStmt.setInt(3, voteValue);
                insertStmt.executeUpdate();
            }

            PreparedStatement sumStmt = conn.prepareStatement(
                "SELECT SUM(vote) as total FROM review_votes WHERE review_id = ?");
            sumStmt.setInt(1, reviewId);
            ResultSet totalRS = sumStmt.executeQuery();

            int totalWeight = 0;
            if (totalRS.next()) {
                totalWeight = totalRS.getInt("total");
            }

            PreparedStatement updateWeight = conn.prepareStatement(
                "UPDATE reviews SET weightage = ? WHERE id = ?");
            updateWeight.setInt(1, totalWeight);
            updateWeight.setInt(2, reviewId);
            updateWeight.executeUpdate();

        } catch (SQLException e) {
            System.out.println(" Failed to process vote: " + e.getMessage());
        }
    }
    
    /**
	* Gets the votes of a review
	*
	* @param reviewId the ID of the review
	* @return the votes
	* 
	*/
    public int getReviewVotes(int reviewId) {
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT SUM(vote) FROM review_votes WHERE review_id = ?")) {
            stmt.setInt(1, reviewId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    
}
