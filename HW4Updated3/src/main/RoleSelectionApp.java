package main;
/*******
 * <p> Title: RoleSelectionApp Class. </p>
 * 
 * <p> Description: Class that launches the user login to begin the application. </p>
 * @author Jacqui Person
 * 
 */
import javafx.application.Application;
import javafx.stage.Stage;

public class RoleSelectionApp extends Application {

	/**
	* Initializes database and starts the primary stage of user login.
	*
	* @param primaryStage the first javaFX stage
	* 
	*/
	@Override
	public void start(Stage primaryStage) throws Exception {
	    //DatabaseClear.deleteDatabaseFile();
		DatabaseManager.initializeDatabase();
	    new UserLogin().show(primaryStage);
	}

	/**
	* Main function.
	*
	* @param args arguments
	* 
	*/
    public static void main(String[] args) {
        launch(args);
    }
}
