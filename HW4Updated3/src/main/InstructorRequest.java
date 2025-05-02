package main;
/*******
 * <p> Title: InstructorRequest Class. </p>
 * 
 * <p> Description: Class that represents an instructor request, including its text, corresponding instructor
 * username, and its corresponding admin username. <p>
 * @author Jacqui Person
 * 
 */

public class InstructorRequest {
	/** Instructor Request Text */
	public String text;
	/** Instructor's Username */
	public String fromInstructor;
	/** Admin's Username */
	public String toAdmin;
	/** Status of request's completion*/
	public String status;
	
  /**
  	* Default constructor.
	*
	*/
	public InstructorRequest() {}
	
	/**
  	* The constructor for an InstructorRequest.
	* 
	* @param text the text
	* @param fromInstructor the instructor's username
	* @param toAdmin to admin's username
	* 
	*/
	public InstructorRequest(String fromInstructor, String toAdmin, String text, String status)
	{
		this.fromInstructor = fromInstructor;
		this.toAdmin = toAdmin;
		this.text = text;
		this.status = status;
	}
	
	/**
	* Sets the text
	*
	* @param text the text.
	* 
	*/
	public void setText(String text)
	{
		this.text = text;
	}
	
	/**
	* Sets the instructor username.
	*
	* @param fromInstructor the instructor's username
	* 
	*/
	public void setFromInstructor(String fromInstructor)
	{
		this.fromInstructor = fromInstructor;
	}
	
	/**
	* Sets the admin's username.
	*
	* @param toAdmin the admin's username
	* 
	*/
	public void setToAdmin(String toAdmin)
	{
		this.toAdmin = toAdmin;
	}
	
	/**
	* Sets the status of the request.
	* 
	* @param status the status to set for the request
	* 
	*/
	public void setStatus(String status)
	{
		this.status = status;
	}
	
	/**
	* Returns the text.
	*
	* @return text the text
	* 
	*/
	public String getText()
	{
		return text;
	}

	/**
	* Returns the instructor's username.
	*
	* @return fromInstructor the instructor's username
	* 
	*/
	public String getFromInstructor()
	{
		return fromInstructor;
	}

	/**
	* Returns the admin's username.
	*
	* @return toAdmin the admin's username
	* 
	*/
	public String getToAdmin()
	{
		return toAdmin;
	}
	
	/**
	* Returns the status of the request.
	*
	* @return status the status
	* 
	*/
	public String getStatus()
	{
		return status;
	}
	
}


