package main;
/*******
 * <p> Title: InstructorController Class. </p>
 * 
 * <p> Description: Class that manages requests to become a reviewer on the 
 * Instructor's end. </p>
 * @author Jacqui Person
 * 
 */
import exception.ValidationException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.*;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Popup;
import javafx.stage.Stage;


public class InstructorController {
	/** User object */
	private User currentUser;
	
	/** ArrayList of pending requests */
	private List<String> pendingRequests = new ArrayList<>();
	
	/**
	* The constructor for an InstructorController.
	*
	* @param currentUser the desired current user 
	* 
	*/
	public InstructorController(User currentUser) {
		this.currentUser = currentUser;
	}
	
	/**
	* Approves student requests to become a reviewer
	*
	* @param studentUsername the username of the student to approve
	* 
	*/
	public void approveRequest(String studentUsername) {
	    try (Connection conn = DatabaseManager.getConnection();
	         PreparedStatement stmt = conn.prepareStatement("UPDATE reviewer_requests SET status = 'accepted' WHERE student_username = ?")) {
	        stmt.setString(1, studentUsername);
	        stmt.executeUpdate();
	    } 
	    catch (SQLException e) {
	        e.printStackTrace();
	    }
	}

	/**
	* Rejects student requests to become a reviewer
	*
	* @param studentUsername the username of the student to reject
	* 
	*/
	public void rejectRequest(String studentUsername) {
	    try (Connection conn = DatabaseManager.getConnection();
	         PreparedStatement stmt = conn.prepareStatement("UPDATE reviewer_requests SET status = 'rejected' WHERE student_username = ?")) {
	        stmt.setString(1, studentUsername);
	        stmt.executeUpdate();
	    } 
	    catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	/**
	* Gets the pending reviewer requests.
	*/
	public List<String> loadPendingRequests() {
	    try (Connection conn = DatabaseManager.getConnection();
	         PreparedStatement stmt = conn.prepareStatement("SELECT student_username FROM reviewer_requests WHERE status = 'pending'")) {
	        ResultSet rs = stmt.executeQuery();
	        while (rs.next()) {
	            pendingRequests.add(rs.getString("student_username"));
	        }
	    } 
	    catch (SQLException e) {
	        e.printStackTrace();
	    }
	    
	    return pendingRequests;
	}
	
	/**
	* Shows the Instructor all the reviewer requests, shows the 
	* accept and reject buttons.
	*
	* @param primaryStage the javaFX stage to display the requests
	* 
	*/
	public void showInstructorRequests(Stage primaryStage) {
	    ListView<String> requestList = new ListView<>();
	    requestList.getItems().addAll(pendingRequests);

	    Button approveButton = new Button("Approve");
	    approveButton.setOnAction(e -> {
	        String selectedRequest = requestList.getSelectionModel().getSelectedItem();
	        if (selectedRequest != null) {
	            approveRequest(selectedRequest);
	            requestList.getItems().remove(selectedRequest);
	        }
	    });

	    Button rejectButton = new Button("Reject");
	    rejectButton.setOnAction(e -> {
	        String selectedRequest = requestList.getSelectionModel().getSelectedItem();
	        if (selectedRequest != null) {
	            rejectRequest(selectedRequest);
	            requestList.getItems().remove(selectedRequest);
	        }
	    });

	    VBox layout = new VBox(10, new Label("Pending Reviewer Requests:"), requestList, approveButton, rejectButton);
	    primaryStage.setScene(new Scene(layout, 400, 300));
	    primaryStage.show();
	}

}
