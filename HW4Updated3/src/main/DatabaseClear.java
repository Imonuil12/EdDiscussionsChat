package main;
/*******
 * <p> Title: DatabaseClear Class. </p>
 * 
 * <p> Description: Class for clearing the entire database. </p>
 * @author Jacqui Person
 * 
 */
import java.io.File;


public class DatabaseClear {
	
	/**
	* Deletes the whole database file.
	* 
	*/
    public static void deleteDatabaseFile() {
        File dbFile = new File("./data/studentqa.mv.db");
        if (dbFile.exists()) {
            if (dbFile.delete()) {
                System.out.println("Database file deleted successfully.");
            } else {
                System.out.println("Failed to delete database file.");
            }
        } else {
            System.out.println("Database file not found.");
        }
    }

    /**
	* Main to call the deletion function.
	* 
	*/
    public static void main(String[] args) {
        deleteDatabaseFile();
    }
}
