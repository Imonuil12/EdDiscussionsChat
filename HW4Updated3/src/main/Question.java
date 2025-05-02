package main;
/*******
 * <p> Title: Question Class. </p>
 * 
 * <p> Description: Class that represents a question. Includes its question ID, text, 
 * student ID, resolved status, parentQuestionId, keywords, and associated answers  </p>
 * @author Jacqui Person
 * 
 */
import java.util.ArrayList;
import java.util.List;

import javafx.scene.control.ListCell;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class Question {
	/** Question ID */
	private int id;
	/** Question text */
	private String text;
	/** Student ID */
	private String studentId;
	/** isResolved boolean value */
	private boolean isResolved;
	/** Parent question ID */
	private Integer parentQuestionId;
	/** Question keywords list */
	private List<String> keywords;

   /**
	* The default constructor for a Question. It sets the question ID, text, and student ID.
	*/
	public Question(int id, String text, String studentId) {
		this(id, text, studentId, null);
	}

   /**
	* The constructor for a Question. It sets the question ID, text, student ID,
	* resolved status, parent question ID and creates a new keywords ArrayList for the question.   
	*/
	public Question(int id, String text, String studentId, Integer parentQuestionId) {
		this.id = id;
		this.text = text;
		this.studentId = studentId;
		this.isResolved = false;
		this.parentQuestionId = parentQuestionId;
		this.keywords = new ArrayList<>();
	}

	/**
	* Returns the question ID.
	*
	* @return id the question ID
	* 
	*/
	public int getId() {
		return id;
	}

	/**
	* Returns question text.
	*
	* @return text the question text
	* 
	*/
	public String getText() {
		return text;
	}

	/**
	* Sets the question text.
	*
	* @param text the question text to be set
	* 
	*/
	public void setText(String text) {
		this.text = text;
	}

	/**
	* Returns the associated student ID.
	*
	* @return studentId the ID of the student associated with question
	* 
	*/
	public String getStudentId() {
		return studentId;
	}

	/**
	* Returns whether the question is resolved or not.
	*
	* @return isResolved the resolved boolean value
	* 
	*/
	public boolean isResolved() {
		return isResolved;
	}

	/**
	* Sets the isResolved boolean value.
	*
	* @param resolved true or false value that represents whether question is resolved or not
	* 
	*/
	public void setResolved(boolean resolved) {
		isResolved = resolved;
	}

	/**
	* Returns the parent question ID.
	*
	* @return parentQuestionId the ID of the parent question to the current question
	* 
	*/
	public Integer getParentQuestionId() {
		return parentQuestionId;
	}
	
	/**
	* Sets the parent question ID.
	*
	* @param parentQuestionId the ID of the parent question to be associated with current question
	* 
	*/
	public void setParentQuestionId(Integer parentQuestionId) {
		this.parentQuestionId = parentQuestionId;
	}

	/**
	* Returns the ArrayList of keywords for the question.
	*
	* @return keywords the ArrayList of keyword
	* 
	*/
	public List<String> getKeywords() {
		return keywords;
	}
	
	/**
	* Sets the ID of the question.
	*
	* @return id the question ID
	* 
	*/
	public void setId(int id) {
	    this.id = id;
	}


	/**
	* Sets the list of keywords for the question.
	*
	* @param keywords the list of keywords to be associated with the question
	* 
	*/
	public void setKeywords(List<String> keywords) {
		this.keywords = keywords;
	}

	/**
	* Returns the javaFX BorderPane for question
	*
	* @return pane the question BorderPane 
	* 
	*/
	public BorderPane getBox() {
		BorderPane pane = new BorderPane();
		VBox texts = new VBox();
		ToggleButton showAnswers = new ToggleButton();
		showAnswers.setText(" |\nV");
		Text user = new Text(studentId);
		user.setStyle("-fx-font-weight: bold");
		Text contents = new Text(text);
		contents.setStyle("-fx-font: 24 arial;");
		texts.getChildren().addAll(user, contents);
		pane.setCenter(texts);
		pane.setLeft(showAnswers);
		pane.setUserData(this);
		return pane;
	}

}