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
Demonstration of the program:
**Demonstration** – [Youtube](https://www.youtube.com/watch?v=jgpKUSDOCPY)
Author
**Imonuil Suleimanov** – [GitHub Profile](https://github.com/Imonuil12)
