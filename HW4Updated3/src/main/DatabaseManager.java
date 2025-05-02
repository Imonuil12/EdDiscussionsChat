package main;
/*******
 * <p> Title: DatabaseManager Class. </p>
 * 
 * <p> Description: Class that initializes the database. </p>
 * @author Jacqui Person
 * 
 */
import java.sql.*;

public class DatabaseManager {
	/** Database URL */
    private static final String DB_URL = "jdbc:h2:./data/studentqa;AUTO_SERVER=TRUE";
    /** Database username */
    private static final String DB_USERNAME = "sa";
    /** Database password */
    private static final String DB_PASSWORD = "";

	/**
	* Attempts to establish connection to database URL DB_URL. 
	*
	* @return DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD) the driver for the connection 
	* 
	*/
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
    }

    /**
	* Attempts to initialize the database and prints whether or not it was successful. 
	*/
    public static void initializeDatabase() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS questions (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    text VARCHAR(1000),
                    student_id VARCHAR(255)
                );
            """);

            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS answers (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    question_id INT,
                    text VARCHAR(1000),
                    student_id VARCHAR(255)
                );
            """);

            stmt.executeUpdate("""
            	    CREATE TABLE IF NOT EXISTS reviews (
            	        id INT AUTO_INCREMENT PRIMARY KEY,
            	        answer_id INT,
            	        reviewer VARCHAR(255),  
            	        text TEXT,
            	        weightage INT,
            	        previous_review_id INT,
            	        FOREIGN KEY (previous_review_id) REFERENCES reviews(id) ON DELETE SET NULL
            	    );
            	""");

            stmt.executeUpdate("""
            	    CREATE TABLE IF NOT EXISTS users (
            	        username VARCHAR(255) PRIMARY KEY,
            	        password VARCHAR(255),
            	        role VARCHAR(50),
            	        has_profile VARCHAR(50),
            	        biography VARCHAR(255),
            	        display_name VARCHAR(255)
            	    );
            	""");
            
            stmt.executeUpdate("""
            	    CREATE TABLE IF NOT EXISTS trusted_reviewers (
            	        student VARCHAR,
            	        reviewer VARCHAR
            	    );
            	""");
            
            stmt.executeUpdate("""
            		CREATE TABLE IF NOT EXISTS reviewer_requests (
            			student_username VARCHAR(255),
            			status VARCHAR(20),  -- 'pending', 'accepted', 'rejected'
            			PRIMARY KEY (student_username)
            		);
            	 """);
            
            stmt.executeUpdate("""
            		CREATE TABLE IF NOT EXISTS messages (
            			from_username VARCHAR(255),
            			to_username VARCHAR(255), 
            			text VARCHAR
            		);
            	 """);
            
            stmt.executeUpdate("""
            		CREATE TABLE IF NOT EXISTS instructors_requests (
            			instructor_username VARCHAR(255),
            			admin_username VARCHAR(255), 
            			text TEXT,
            			status VARCHAR(20)
            		);
            	 """);
            
            stmt.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS private_feedback (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        reviewer VARCHAR,
                        student VARCHAR,
                        feedbackText VARCHAR
                    );
                """);
            
            System.out.println("Database initialized.");

        } catch (SQLException e) {
            System.out.println("Failed to initialize database: " + e.getMessage());
        }
    }
}
