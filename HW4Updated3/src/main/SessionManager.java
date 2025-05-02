package main;
/*******
 * <p> Title: SessionManager Class. </p>
 * 
 * <p> Description: Class that manages the current user, setting the current one and clearing the current one. </p>
 * @author Jacqui Person
 * 
 */
public class SessionManager {
	/** User object */
    private static User currentUser;

    /**
	* Sets the current user. 
	*
	* @param user the user the be set as the current user
	* 
	*/
    public static void setCurrentUser(User user) {
        currentUser = user;
    }

    /**
	* Returns the current user.
	*
	* @return currentUser the current user
	* 
	*/
    public static User getCurrentUser() {
        return currentUser;
    }

    /**
	* Sets the current user to null.
	* 
	*/
    public static void clear() {
        currentUser = null;
    }
}
