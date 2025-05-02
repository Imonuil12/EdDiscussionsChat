package main;
/*******
 * <p> Title: User Class. </p>
 * 
 * <p> Class that represents a user, including the user's username, password, and role. </p>
 * @author Jacqui Person
 * 
 */
public class User {
	/** User's username */
    private String username;
    /** User's password */
    private String password;
    /** User's role */
    private String role;
    /** User has a profile? */
    private String hasProfile;
    /** User's profile biography */
    private String biography;
    /** User's profile display name */
    private String displayName;
    

    /** 
	* The constructor for a User.
	* 
	* @param username the user's username
	* @param password the user's password
	* @param role the user's role
	*/
    public User(String username, String password, String role, String hasProfile, String biography, String displayName) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.hasProfile = "false";
        this.biography = biography;
        this.displayName = displayName;
    }

    /**
	* Returns the user's username.
	*
	* @return username the user's username
	* 
	*/
    public String getUsername() { return username; }
    
    /**
	* Returns the user's password.
	*
	* @return password the user's password.
	* 
	*/
    public String getPassword() { return password; }
    
    /**
	* Returns the user's role.
	*
	* @return role the user's role
	* 
	*/
    public String getRole() { return role; }
    
    /**
   	* Returns the user's profile status.
   	*
   	* @return hasProfile the profile status
   	* 
   	*/
    public String getHasProfile() { return hasProfile; }
    
    /**
   	* Returns the user's profile bio.
   	*
   	* @return biography the user's bio
   	* 
   	*/
    public String getBiography() { return biography; }
    
    /**
   	* Returns the user's profile display name.
   	*
   	* @return displayName the display name
   	* 
   	*/
    public String getDisplayName() { return displayName; }
    
    /**
	* Sets the user's role.
	*
	* @param role the role to set for the user
	* 
	*/
    public void setRole(String role) { this.role = role; }
    
    /**
	* Sets the user's profile status.
	*
	* @param status of whether or not the user has a profile already
	* 
	*/
    public void setHasProfile(String status) { this.hasProfile = status; }
    
    /**
	* Sets the user's profile bio.
	*
	* @param biography the bio
	* 
	*/
    public void setBiography(String biography) { this.biography = biography; }
    
    /**
	* Sets the user's profile display name.
	*
	* @param displayName the display name
	* 
	*/
    public void setDisplayName(String displayName) { this.displayName = displayName; }
}
