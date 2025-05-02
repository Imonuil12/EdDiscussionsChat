package main;
/*******
 * <p> Title: Message Class. </p>
 * 
 * <p> Description: Class that represents a message including its text, corresponding 
 * sender username and corresponding recipient username. Only members of staff, admin,
 * and instructors can send private messages to each other. <p>
 * @author Jacqui Person
 * 
 */
public class Message {
	
	/** Message Text */
	private String text;
	/** Sender Username */
	private String fromUsername;
	/** Recipient Username */
	private String toUsername;
	
  /**
  	* Default constructor.
	*
	*/
	public Message() {};
	
	/**
  	* The constructor for a Message.
	* 
	* @param text the text
	* @param fromUsername the sender's username
	* @param toUsername the recipient's username
	* 
	*/
	public Message(String text, String fromUsername, String toUsername)
	{
		this.text = text;
		this.fromUsername = fromUsername;
		this.toUsername = toUsername;
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
	* Returns the sender's username.
	*
	* @return fromUsername the sender's username
	* 
	*/
	public String getFromUsername()
	{
		return fromUsername;
	}
    
	/**
	* Returns the recipient's username.
	*
	* @return toUsername to recipient's username
	* 
	*/
	public String getToUsername()
	{
		return toUsername;
	}

}


