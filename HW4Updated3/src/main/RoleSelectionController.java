package main;
/*******
 * <p> Title: RoleSelectionController Class. </p>
 * 
 * <p> Description: Class that launches the login screen to begin the application. </p>
 * @author Jacqui Person
 * 
 */
import javafx.fxml.FXML;
import javafx.stage.Stage;

public class RoleSelectionController {

	/**
	* Initializes database and starts a new stage of user login.
	*/
    @FXML
    private void initialize() throws Exception {
    	DatabaseManager.initializeDatabase();
        new UserLogin().show(new Stage());
    }
}
