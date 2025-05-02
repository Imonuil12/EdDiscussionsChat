package main;
/*******
 * <p> Title: PrivateFeedback Class. </p>
 * 
 * <p> Description: Class that represents private feedback. </p>
 * @author Jacqui Person
 * 
 */
public class PrivateFeedback {
    private String fromStudent;
    private String toReviewer;
    private String feedbackText;

    /**
	* The constructor for a PrivateFeedback.
	*
	* @param id the feedback ID
	* @param fromStudent the student
	* @param toReviewer the reviewer
	* @param feedbackText the text of the feedback
	* 
	*/
    public PrivateFeedback(String toReviewer, String fromStudent, String feedbackText) {
        this.fromStudent = fromStudent;
        this.toReviewer = toReviewer;
        this.feedbackText = feedbackText;
    }
    /**
   	* Returns student.
   	* 
   	* @return fromStudent the student
   	* 
   	*/
    public String getFromStudent() {
        return fromStudent;
    }

    /**
   	* Returns reviewer.
   	* 
   	* @return toReviewer the reviewer
   	* 
   	*/
    public String getToReviewer() {
        return toReviewer;
    }

    /**
   	* Returns feedback Text.
   	* 
   	* @return feedbackText the text of feedback
   	* 
   	*/
    public String getFeedbackText() {
        return feedbackText;
    }
}
