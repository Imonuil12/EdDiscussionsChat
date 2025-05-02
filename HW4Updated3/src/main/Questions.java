package main;
/*******
 * <p> Title: Questions Class. </p>
 * 
 * <p> Description: Class that represents the list of questions, including methods to 
 * create and get questions </p> 
 * @author Jacqui Person
 * 
 */
import exception.ValidationException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Questions {
	/**
	* Creates a new question and saves it to the database if it's valid. 
	*
	* @param question the new question object to be added to the database
	* 
	*/
	public void createQuestion(Question question) throws ValidationException {
		if (question.getText() == null || question.getText().trim().length() < 5) {
            throw new ValidationException("Question must be at least 5 characters long");
        }

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
            	"INSERT INTO questions (text, student_id) VALUES (?, ?)",
                 Statement.RETURN_GENERATED_KEYS)) {

            	stmt.setString(1, question.getText());
            	stmt.setString(2, question.getStudentId());
            	stmt.executeUpdate();

            	try (ResultSet rs = stmt.getGeneratedKeys()) {
            		if (rs.next()) {
            			question.setId(rs.getInt(1));
            		}
            	}
        } 
        catch (SQLException e) {
            throw new ValidationException("Failed to save question to database: " + e.getMessage());
        }
    }

	/**
	* Returns the ArrayList of all questions in the database.
	* 
	* @return list the ArrayList of all questions in the database. 
	* 
	*/
    public List<Question> getAllQuestions() {
        List<Question> list = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection();
        	Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM questions")) {

            while (rs.next()) {
            	int id = rs.getInt("id");
                String text = rs.getString("text");
                String studentId = rs.getString("student_id");
                Question q = new Question(id, text, studentId);
                list.add(q);
            }

        } 
        catch (SQLException e) {
            System.out.println("Failed to fetch questions: " + e.getMessage());
        }
        return list;
    }
    
    /**
	* Returns the question associated to the question ID.
	* 
	* @param id the question ID of the question to search for
	* @return question the question
	* 
	*/
    public Question getQuestionById(int id) {
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM questions WHERE id = ?")) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Question(
                    rs.getInt("id"),
                    rs.getString("text"),
                    rs.getString("student_id")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
