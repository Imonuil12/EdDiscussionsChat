package main;
/*******
 * <p> Title: Review Class. </p>
 * 
 * <p> Description: Class that represents a review, including its ID, corresponding answer ID, 
 * its text, its corresponding reviewer ID, its weightage, and its previous review ID </p>
 * @author Jacqui Person
 * 
 */
public class Review {
	/** Review ID */
    private int id;
    /** Answer ID */
    private int answerId;
    /** Reviewer ID */
    private String reviewerId;
    /** Review text */
    private String text;
    /** Review weightage */
    private int weightage;
    /** ID of previous review*/
    private Integer previousReviewId; 

    /**
	 * The constructor for a Review. 
	 * 
	 * @param id the review ID
	 * @param answerId the answer ID associated with the review
	 * @param reviewerId the ID of reviewer associated with the review
	 * @param text review text
	 * @param weightage the review weightage
	 * @param previousReviewId the ID of the previous review
	 */
    public Review(int id, int answerId, String reviewerId, String text, int weightage, Integer previousReviewId) {
        this.id = id;
        this.answerId = answerId;
        this.reviewerId = reviewerId;
        this.text = text;
        this.weightage = weightage;
        this.previousReviewId = previousReviewId;
    }

    /**
	* Returns the review ID.
	*
	* @return id the review ID
	* 
	*/
    public int getId() {
        return id;
    }

    /**
	* Returns the answer ID.
	*
	* @return answerId the answer ID
	* 
	*/
    public int getAnswerId() {
        return answerId;
    }

    /**
	* Returns the reviewer ID
	*
	* @return reviwerId the reviewer's ID
	* 
	*/
    public String getReviewerId() {
        return reviewerId;
    }

    /**
	* Returns the review's text
	*
	* @return text the text of the review
	* 
	*/
    public String getText() {
        return text;
    }

    /**
	* Returns the review's weightage
	*
	* @return weightage the review's weightage
	* 
	*/
    public int getWeightage() {
        return weightage;
    }
    
    /**
	* Returns the previous review ID
	*
	* @return previousReviewId the ID of the previous review
	* 
	*/
    public Integer getPreviousReviewId() {
        return previousReviewId;
    }
}
