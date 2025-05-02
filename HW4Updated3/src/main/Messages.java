package main;
/*******
 * <p> Title: Messages Class. </p>
 * 
 * <p> Description: Class that manages messages, including
 * methods to create them and get lists of them. </p>
 * @author Jacqui Person
 * 
 */
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import exception.ValidationException;

public class Messages {
	
	public Messages() {};
	
  /**
	* Creates a new Message and saves it to the database.
	*
	* @param m the message to save
	* 
	*/
	public void createMessage(Message m) throws ValidationException
	{
		 try (Connection conn = DatabaseManager.getConnection();
	             PreparedStatement stmt = conn.prepareStatement(
	            	"INSERT INTO messages (from_username, to_username, text) VALUES (?, ?, ?)"))
		 {
			 stmt.setString(1, m.getFromUsername());
			 stmt.setString(2, m.getToUsername());
			 stmt.setString(3, m.getText());
			 stmt.executeUpdate();
		 }
		 catch (SQLException e) {
	            throw new ValidationException("Failed to save message to database: " + e.getMessage());
	        }
	}
	
	/**
	* Returns a list of Messages whose recipient matches the parameter.
	*
	* @param to_username the recipient to search for
	* @returns list the list of the messages
	* 
	*/
	public List<Message> getMessagesByRecipient(String to_username)
	{
		 List<Message> list = new ArrayList<>();
		 try (Connection conn = DatabaseManager.getConnection();
	            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM messages WHERE to_username = ?")) {
	            stmt.setString(1, to_username);
	            ResultSet rs = stmt.executeQuery();
	            while (rs.next()) {
	            	String fromUsername = rs.getString("from_username");
	            	String toUsername = rs.getString("to_username");
	            	String text = rs.getString("text");
	                Message m = new Message(text, fromUsername, toUsername);
	                list.add(m);
	            }
	           
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return list;
	}
	
	/**
	* Returns a list of all Messages in the database.
	* 
	* @returns list the list of the messages
	* 
	*/
	public List<Message> getAllMessages()
	{
		List<Message> list = new ArrayList<>();
		 try (Connection conn = DatabaseManager.getConnection();
	            Statement stmt = conn.createStatement();
	            ResultSet rs = stmt.executeQuery("SELECT * FROM messages")) {
	            while (rs.next()) {
	            	String fromUsername = rs.getString("from_username");
	            	String toUsername = rs.getString("to_username");
	            	String text = rs.getString("text");
	                Message m = new Message(text, fromUsername, toUsername);
	                list.add(m);
	            }
	           
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return list;
	}

}
