package main;
/*******
 * <p> Title: UserLogin Class. </p>
 * 
 * <p> Class contains the methods in order to display and manage the user login functionality. </p>
 * @author Jacqui Person
 * 
 */
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.*;

public class UserLogin {

   /**
	* Displays the first stage of user login. Includes the possible roles,
	* login and register options as well as authentication. 
	*
	* @param primaryStage the first javaFX stage. 
    * 
	*/
	public void show(Stage primaryStage) {
        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        ChoiceBox<String> roleBox = new ChoiceBox<>();
        roleBox.getItems().addAll("Admin", "Staff", "Student", "Reviewer", "Instructor");
        roleBox.setValue("Student");

        Button loginBtn = new Button("Login");
        Button registerBtn = new Button("Register");

        Label message = new Label();
        message.setStyle("-fx-text-fill: red;");

        loginBtn.setOnAction(e -> {
            String user = usernameField.getText().trim();
            String pass = passwordField.getText().trim();

            if (authenticate(user, pass)) {
                User loggedIn = new User(user, pass, getRole(user), getHasProfile(user), getBiography(user), getDisplayName(user));
                SessionManager.setCurrentUser(loggedIn);
                launchQA(primaryStage, loggedIn);
            } else {
                message.setText("Invalid credentials!");
            }
        });

        registerBtn.setOnAction(e -> {
            String user = usernameField.getText().trim();
            String pass = passwordField.getText().trim();
            String role = roleBox.getValue();

            if (register(user, pass, role, "", "", "")) {
                message.setStyle("-fx-text-fill: green;");
                message.setText("Registered! Please login.");
            } else {
                message.setText("Username already exists!");
            }
        });

        VBox layout = new VBox(10, new Label("Login / Register"), usernameField, passwordField, roleBox, loginBtn, registerBtn, message);
        layout.setStyle("-fx-padding: 25;");
        primaryStage.setScene(new Scene(layout, 300, 300));
        primaryStage.setTitle("Student QA - Login");
        primaryStage.show();
    }
	
   /**
	* Authenticates the username and password of the user and returns a boolean representing
	* its success. 
	*
	* @param username the username to be authenticated
	* @param password the password to be authenticated
	* @return false if the authentication was unsuccessful
	* 
	*/
    private boolean authenticate(String username, String password) {
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE username = ? AND password = ?")) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            return stmt.executeQuery().next();
        } 
        catch (SQLException e) {
        	e.printStackTrace();
            return false;
        }
    }
    
    /**
 	* Inserts new user into the database.
 	*
 	* @param username the username of user to be added 
 	* @param password the password of the user to be added
 	* @param role the role of the user to be added
    * @return false if registration was unsuccessful
 	*/
    boolean register(String username, String password, String role, String has_profile, String biography, String displayName) {
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement check = conn.prepareStatement("SELECT * FROM users WHERE username = ?")) {
            check.setString(1, username);
            ResultSet rs = check.executeQuery();
            if (rs.next())
            {
            	return false;
            }

            PreparedStatement insert = conn.prepareStatement("INSERT INTO users (username, password, role, has_profile, biography, display_name) VALUES (?, ?, ?, ?, ?, ?)");
            insert.setString(1, username);
            insert.setString(2, password);
            insert.setString(3, role);
            insert.setString(4, has_profile);
            insert.setString(5, biography);
            insert.setString(6, displayName);
            insert.executeUpdate();
            return true;
        } 
        catch (SQLException e) {
        	e.printStackTrace();
            return false;
        }
    }

    /**
 	* Returns the role of the user with a specific username.
 	*
 	* @param username the username of the user to get the role of
    * @return "Student" when there is an SQLException
 	*/
    private String getRole(String username) {
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT role FROM users WHERE username = ?")) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            return rs.next() ? rs.getString("role") : "Student";
        } 
        catch (SQLException e) {
        	e.printStackTrace();
            return "Student";
        }
    }
    
    /**
 	* Returns the the boolean of whether or not the current user has a profile.
 	*
 	* @param username the username of the user to get the hasProfile of 
    * @return "" when there is an SQLException
 	*/
    private String getHasProfile(String username) {
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT has_profile FROM users WHERE username = ?")) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            return rs.next() ? rs.getString("has_profile") : "";
        } 
        catch (SQLException e) {
        	e.printStackTrace();
            return "";
        }
    }
    
    /**
 	* Returns the the current user's biography.
 	*
 	* @param username the username of the user to get the biography of 
    * @return "" when there is an SQLException
 	*/
    private String getBiography(String username) {
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT biography FROM users WHERE username = ?")) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            return rs.next() ? rs.getString("biography") : "";
        } 
        catch (SQLException e) {
        	e.printStackTrace();
            return "";
        }
    }

    
    /**
 	* Returns the the current user's display name for their profile.
 	*
 	* @param username the username of the user to get the biography of 
    * @return "" when there is an SQLException
 	*/
    private String getDisplayName(String username) {
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT display_name FROM users WHERE username = ?")) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            return rs.next() ? rs.getString("display_name") : "";
        } 
        catch (SQLException e) {
        	e.printStackTrace();
            return "";
        }
    }
    
    /**
 	* Launches the SceneController and starts at the specified stage. 
 	*
 	* @param stage the stage to begin the controller
 	* @param user the user to associate with the new SceneController
    * 
 	*/
    private void launchQA(Stage stage, User user) {
        try {
            SceneController controller = new SceneController(user);
            controller.start(stage);
        } 
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
