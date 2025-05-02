# Student Question and Answer System – HW4

This is an academic JavaFX-based application. The application supports four roles: **Students**, **Reviewers**, **Instructors**, and **Staff**, each with tailored functionality for managing and interacting with questions, answers, and reviews.

## 👤 Roles and Features

### Student
- Ask questions and answer others' questions
- Trust reviewers and send private feedback
- Search for answered, unanswered questions, or reviewers

### Reviewer
- Write and update reviews for answers
- View feedback sent by students
- Receive flagged questions from staff for review

### Instructor
- View and approve reviewer requests
- See pending requests from students

### Staff
- View and moderate all questions and answers
- Flag inappropriate content using keyword matching
- Assign questions to reviewers with a message
- Delete inappropriate content (questions, answers)
- View private feedback exchanged between users

## ✅ JUnit Testing
The application includes unit tests for core features like:
- Trusted reviewer flow
- Staff assignment logic
- Answer and question moderation
- Review creation and updates

Run the tests using:
```
./gradlew test
```
or configure through your IDE (e.g., IntelliJ, Eclipse).

## 📄 Javadoc
The project includes Javadoc for major files like:
- `SceneController.java`
- `StaffAssignmentController.java`
- `TrustedReviewers.java`
To generate:
```
javadoc -d docs/ -sourcepath src/ main/StaffAssignmentController.java
```

## 🛠️ Setup Requirements

Before running the project, make sure to set up the required environment and libraries:

### ✅ Java SDK
- Java SE Development Kit (JDK) 17 or later
- [Download JDK](https://www.oracle.com/java/technologies/javase-downloads.html)

### ✅ Required Libraries
The project uses the following external libraries:

1. **JavaFX SDK** (for UI)
   - javafx.controls
   - javafx.fxml
   - javafx.graphics
   - javafx.base
   - [JavaFX Download](https://gluonhq.com/products/javafx/)

2. **H2 Database Engine**
   - For local data persistence and embedded database usage
   - [H2 Download](https://www.h2database.com/)

3. **JUnit 5**
   - For running unit tests
   - org.junit.jupiter.api
   - [JUnit 5 Guide](https://junit.org/junit5/)

### 📦 How to Add Libraries (Example - IntelliJ IDEA)
1. Open Project Structure (File → Project Structure → Modules → Dependencies)
2. Click "+" → JARs or directories → Add `javafx-sdk/lib` and `h2.jar`
3. Apply changes

Make sure VM options include JavaFX modules, e.g.:
```
--module-path /path/to/javafx-sdk/lib --add-modules javafx.controls,javafx.fxml
```


Demonstration of the program:
**Demonstration** – [Youtube](https://www.youtube.com/watch?v=jgpKUSDOCPY)
Author
**Imonuil Suleimanov** – [GitHub Profile](https://github.com/Imonuil12)
