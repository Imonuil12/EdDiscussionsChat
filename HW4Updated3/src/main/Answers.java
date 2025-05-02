package main;
/*******
 * <p> Title: Answers Class. </p>
 * 
 * <p> Description: Class for managing answers, including methods to create, get, update, 
 * delete, accept and check answers </p>
 * @author Jacqui Person
 * 
 */
import exception.ValidationException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Answers {
	/**
	* Creates a new answer and saves it to the database if it's valid. 
	*
	* @param answer the new answer object to be added to the database
	* 
	*/
	public void createAnswer(Answer answer) throws ValidationException {
		if (answer.getText() == null || answer.getText().trim().length() < 2) {
			throw new ValidationException("Answer must be at least 2 characters long");
        }

        try (Connection conn = DatabaseManager.getConnection();
        	PreparedStatement stmt = conn.prepareStatement("INSERT INTO answers (question_id, text, student_id) VALUES (?, ?, ?)",
            Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, answer.getQuestionId());
            stmt.setString(2, answer.getText());
            stmt.setString(3, answer.getStudentId());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
            	if (rs.next()) {
            		int generatedId = rs.getInt(1);
            		answer.setId(generatedId);
                }
            }
        }  
        catch (SQLException e) {
        		throw new ValidationException("Failed to save answer to database: " + e.getMessage());
        }
    }

	/**
	* Returns the ArrayList of answers to a specific question.
	*
	* @param questionId the ID of the specific question
	* @return answers the ArrayList of all answers to the specific question
	* 
	*/
	public List<Answer> getAnswersForQuestion(int questionId) {
		List<Answer> answers = new ArrayList<>();
		try (Connection conn = DatabaseManager.getConnection();
			PreparedStatement stmt = conn.prepareStatement("SELECT * FROM answers WHERE question_id = ?")) {
			stmt.setInt(1, questionId);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				int id = rs.getInt("id");
				String text = rs.getString("text");
				String studentId = rs.getString("student_id");
				Answer answer = new Answer(id, questionId, text, studentId);
				answer.createLabel();
				answers.add(answer);
            }
		} 
		catch (SQLException e) {
			System.out.println("Failed to fetch answers: " + e.getMessage());
		}
        return answers;
    }
	
	/**
	* Returns the ArrayList of all answers in the database	
	* 
	* @return answers the ArrayList of all answers in database
	* 
	*/
	public List<Answer> getAllAnswers() {
	    List<Answer> answers = new ArrayList<>();
	    String query = "SELECT * FROM answers"; // SQL query to get all answers

	    try (Connection conn = DatabaseManager.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(query);
	         ResultSet rs = stmt.executeQuery()) {

	        while (rs.next()) {
	            int id = rs.getInt("id");
	            int questionId = rs.getInt("question_id");
	            String text = rs.getString("text");
	            String studentId = rs.getString("student_id");
	            
	            // Create Answer object
	            Answer answer = new Answer(id, questionId, text, studentId);
	            answers.add(answer); // Add to list
	        }
	    } catch (SQLException e) {
	        System.out.println("Failed to fetch answers: " + e.getMessage());
	    }
	    
	    return answers; 
	}

	/**
	* Returns the number of answers to a question.
	*
	* @param questionId the ID of the question to count the answers for
	* @return getAnswersForQuestion(questionId).size() the number of answers to the question
	* 
	*/
	public int getSizeForQuestion(int questionId) {
		return getAnswersForQuestion(questionId).size();
	}
	
	/**
	* Deletes a specific answer from database.
	*
	* @param answerId the answer to delete
	
	*/
	public void deleteAnswer(int answerId) {
	    try (Connection conn = DatabaseManager.getConnection()) {
	        PreparedStatement deleteReviews = conn.prepareStatement("DELETE FROM reviews WHERE answer_id = ?");
	        deleteReviews.setInt(1, answerId);
	        deleteReviews.executeUpdate();

	        PreparedStatement stmt = conn.prepareStatement("DELETE FROM answers WHERE id = ?");
	        stmt.setInt(1, answerId);
	        stmt.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
}
