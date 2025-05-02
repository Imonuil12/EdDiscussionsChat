package main;
/*******
 * <p> Title: Answer Class. </p>
 * 
 * <p> Description: Class that represents an answer, including its ID, corresponding question ID, 
 * its text, its corresponding student ID, its accepted status, its reviews and its JavaFX label </p>
 * @author Jacqui Person
 * 
 */
import java.util.List;
import exception.ValidationException;
import java.util.ArrayList;
import java.util.List;


import javafx.scene.control.Label;


public class Answer {
	/** Answer ID */
	private int id;
	/** Question ID */
	private int questionId;
	/** Answer text */
	private String text;
	/** Student ID */
	private String studentId;
	/** Accepted status */
	private boolean isAccepted;
	/** Reviews for answer */
	private Reviews reviews;
	/** JavaFX label for answer */
	public Label label;

   /**
	* The constructor for an Answer. 
	* 
	* @param id the answer ID
	* @param questionId the question ID
	* @param text the answer text
	* @param studentId
	* 
	*/
	public Answer(int id, int questionId, String text, String studentId) {
		this.id = id;
		this.questionId = questionId;
		this.text = text;
		this.studentId = studentId;
		this.isAccepted = false;
		this.reviews=new Reviews();
	}

	/**
	* Returns the answer ID.
	*
	* @return id the answer ID
	* 
	*/
	public int getId() {
		return id;
	}

	/**
	* Returns the question ID.
	*
	* @return questionId the question ID
	* 
	*/
	public int getQuestionId() {
		return questionId;
	}
	
	/**
	* Return the answer text.
	*
	* @return text the answer text
	* 
	*/
	public String getText() {
		return text;
	}
	
	
	/**
	* Sets the answer text.
	*
	* @param text the answer text
	* 
	*/
	public void setText(String text) {
		this.text = text;
	}

	/**
	* Returns the student ID.
	*
	* @return studentId the student ID
	* 
	*/
	public String getStudentId() {
		return studentId;
	}

	/**
	* Returns boolean variable isAccepted.
	*
	* @return isAccepted a boolean value that shows whether the answer is accepted
	* 
	*/
	public boolean isAccepted() {
		return isAccepted;
	}

	/**
	* Sets the boolean variable isAccepted.
	*
	* @param accepted a boolean value that determines whether the answer is accepted
	* 
	*/
	public void setAccepted(boolean accepted) {
		isAccepted = accepted;
	}
	
	/**
	* Sets the answerID.
	*
	* @param id the answer ID to be set 
	* 
	*/
	public void setId(int id) {
	    this.id = id;
	}
	
	/**
	* Returns the list of reviews for an answer.
	*
	* @return reviews the list of reviews for a specific answer ID
	* 
	*/
    public List<Review> getReviews() {
        return reviews.getReviewsForAnswer(this.id);
    }

    /**
	* Adds a review to the list of reviews.
	*
	* @param reviewerId ID of reviewer who made the review 
	* @param text text of the review
	* @param weightage integer weightage of the review
	* @param previousReviewId ID of the previous review
	* 
	*/
    public void addReview(String reviewerId, String text, int weightage, Integer previousReviewId) throws ValidationException {
        reviews.createReview(this.id, reviewerId, text, weightage, previousReviewId);
    }

    /**
	* Creates the javaFX label 
	*
	* @return label javaFX label
	* 
	*/
	public Label createLabel() {
		label = new Label(studentId + ": " + text);
		label.setWrapText(true);
		return label;
	}

}