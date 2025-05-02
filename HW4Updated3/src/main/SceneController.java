package main;
/*******
 * <p> Title: SceneController Class. </p>
 * 
 * <p> Description: Class that manages all the JavaFX functions. </p>
 * @author Jacqui Person
 * 
 */
import java.sql.SQLException;
/*******
 * <p> Title: SceneController Class. </p>
 * 
 * <p> Description: Class that controls the javaFX scenes for beginning the 
 * application, creating questions / question pop ups, creating answers and 
 * updating reviews  </p>
 * @author Jacqui Person
 * 
 */
import java.util.*;

import exception.ValidationException;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Popup;
import javafx.stage.Stage;

public class SceneController extends Application {
	/** User object */
    private User currentUser;
    /** Reviews object */
    private Reviews reviews = new Reviews();
    /** Users object */
    private Users users = new Users();
    /** Questions object */
    private Questions questionController = new Questions();
    /** Answers object */
    private Answers answersController = new Answers();
    /** InstructorRequests Object */
    private InstructorRequests instructorRequestsController = new InstructorRequests();
    /** Messages Object */
    private Messages messagesController = new Messages();

    /** ArrayList of topics for questions */
    final List<String> topicsList = Arrays.asList("Homework", "Projects", "Lab");
    /** ArrayList of roles for profiles */
    final List<String> rolesList = Arrays.asList("Admin", "Instructor", "Staff", "Student", "Reviewer");
    /** ArrayList of admins for instructors to request */
    final List<String> adminsList = users.getAllAdmins();
    /** ArrayList of users to send messages */
    final List<String> recipientsList = users.getAllInstructors();
    /** ArrayList of status flags for instructor requests*/
    final List<String> statusList = Arrays.asList("incomplete", "complete", "in progress");
    
    /**Set of flagged words to look for in system */
    private Set<String> flaggedWords = new HashSet<>(Set.of("stupid", "idiot", "hate", "dumb", "nonsense", "shut up", "???", "!!!!", "meh"));
    
    /** BorderPane for scene*/
    private BorderPane border;
    /** Pop up for question window */
    private Popup questionWindow;
    /** Pop up for profile window */
    private Popup profileWindow;
    /** Pop up for instructor request window */
    private Popup instructorRequestWindow;
    /** Pop up for viewing instructor requests window */
    private Popup privateFeedbackWindow;
    
    private Popup createMessageWindow;
    /** Vertical box for question pane */
    private VBox questionPane;
    /** Horizontal box for top pane */
    private HBox topPane;
    /** Answer index, begins at 1 */
    private int answerIndex = 1;
    /** FeedbackController object */
    private FeedbackController feedbackController = new FeedbackController();
    /** TrustedReviewers object */
    private TrustedReviewers trustedController = new TrustedReviewers();
    /** New trusted reviewers set */
    private Set<String> trustedList = new HashSet<>();
   

    /** 
     * Default constructor 
     * */
    public SceneController() {}
    
    /**
   	* The constructor for SceneController class. It initializes the currentUser. 
   	*
   	* @param user the user to make the currentUser
   	* 
   	*/
    public SceneController(User user) {
        this.currentUser = user;
    }

    /**
	* Initializes database and begins primary stage, including add question button.
	*
	* @param primaryStage the first javaFX stage
	* 
	*/
    @Override
    public void start(Stage primaryStage) throws Exception {
        DatabaseManager.initializeDatabase();
        primaryStage.setTitle("Questions Page - User: " + currentUser.getUsername() + " (" + currentUser.getRole() + ")");
        trustedList = trustedController.getTrustedReviewers(currentUser.getUsername());
        
        border = new BorderPane();
        topPane = new HBox();
        topPane.setStyle(Styles.borderTopBGColor);
        questionPane = new VBox(10);

        border.setTop(topPane);
        border.setCenter(questionPane);
        topPane.setSpacing(10);
        topPane.setPadding(new Insets(15));
        questionPane.setPadding(new Insets(15));
        questionPane.setStyle(Styles.questionPaneBGColor);
        
        TextField searchField = new TextField();
        searchField.setPromptText("Search for questions or reviewers...");

        ComboBox<String> filterBox = new ComboBox<>();
        filterBox.getItems().addAll("All Questions", "Answered Questions", "Unanswered Questions", "Reviewers");
        filterBox.setValue("All Questions");
        
        Button b_search = new Button("Search");
        b_search.setOnAction(e -> {
        	   String query = searchField.getText().trim().toLowerCase();
               String filter = filterBox.getValue();
               VBox resultsBox = new VBox(10);
               resultsBox.setPadding(new Insets(15));
               
               if (filter.equals("Reviewers"))
               {
            	   for (String reviewer : trustedController.getAllReviewerUsernames()) {
                       if (reviewer.toLowerCase().contains(query)) {
                           resultsBox.getChildren().add(new Label("Reviewer: " + reviewer));
                       }
                       
            	   }
               }
               else
               {
            	   List<Question> allQs = questionController.getAllQuestions();
                   for (Question q : allQs) {
                       boolean matches = q.getText().toLowerCase().contains(query);
                       boolean isAnswered = !answersController.getAnswersForQuestion(q.getId()).isEmpty();
                       
                       if (matches && (
                               filter.equals("All Questions") ||
                               (filter.equals("Answered Questions") && isAnswered) ||
                               (filter.equals("Unanswered Questions") && !isAnswered)
                           )) 
                       {
                               Label match = new Label("Q: " + q.getText());
                               match.setOnMouseClicked(evt -> border.setRight(createAnswer(q)));
                               resultsBox.getChildren().add(match);
                       }
                   }
               }
               ScrollPane scroll = new ScrollPane(resultsBox);
               scroll.setFitToWidth(true);
               border.setRight(scroll);
        });
        
        HBox searchBox = new HBox(10, searchField, filterBox, b_search);
        topPane.getChildren().add(searchBox);

        Button b_AddQuestion = new Button("Add Question");
        b_AddQuestion.setStyle("-fx-border-color: #0078D7; -fx-background-radius: 5; -fx-padding: 5 10;");
        b_AddQuestion.setOnAction(e -> {
            if (questionWindow == null) {
                questionWindow = showQuestionWindow();
            }
            if (questionWindow.isShowing())
                questionWindow.hide();
            else
                questionWindow.show(primaryStage);
        });
        
        Button b_ViewAllPrivateFeedback = new Button("View All Private Feedback");
        b_ViewAllPrivateFeedback.setStyle("-fx-border-color: #0078D7; -fx-background-radius: 5; -fx-padding: 5 10;");
        b_ViewAllPrivateFeedback.setOnAction(e -> {
        	displayAllFeedback();
       });
        
        Button b_ViewInstructorsRequests =  new Button("View Instructor's Requests");
        b_ViewInstructorsRequests.setStyle("-fx-border-color: #0078D7; -fx-background-radius: 5; -fx-padding: 5 10;");
        b_ViewInstructorsRequests.setOnAction(e -> {
            	displayInstructorRequests();
        });
        
        Button b_ViewMyMessages =  new Button("View My Messages");
        b_ViewMyMessages.setStyle("-fx-border-color: #0078D7; -fx-background-radius: 5; -fx-padding: 5 10;"); 
        b_ViewMyMessages.setOnAction(e -> {
            	displayMyMessages();
        });
        
        Button b_InstructorsRequest =  new Button("Request Something");
        b_InstructorsRequest.setStyle("-fx-border-color: #0078D7; -fx-background-radius: 5; -fx-padding: 5 10;");
        b_InstructorsRequest.setOnAction(e -> {
            if (instructorRequestWindow == null) {
            	instructorRequestWindow  = createInstructorRequest();
            }
            if (instructorRequestWindow.isShowing())
            	instructorRequestWindow.hide();
            else
            	instructorRequestWindow.show(primaryStage);
        });
        
        Button b_MessageSomeone =  new Button("Message Someone");
        b_MessageSomeone.setStyle("-fx-border-color: #0078D7; -fx-background-radius: 5; -fx-padding: 5 10;");
        b_MessageSomeone.setOnAction(e -> {
        	if (createMessageWindow == null) {
        		createMessageWindow  = createMessage();
            }
            if (createMessageWindow.isShowing())
            	createMessageWindow.hide();
            else
            	createMessageWindow.show(primaryStage);
        });
        
        Button b_ViewProfile = new Button("View My Profile");
        b_ViewProfile.setStyle("-fx-border-color: #0078D7; -fx-background-radius: 5; -fx-padding: 5 10;");
        b_ViewProfile.setOnAction(e -> {
             try {
				displayProfile();
			} catch (SQLException e1) {
				
				e1.printStackTrace();
			}
        });
        
        Button b_CreateProfile = new Button("Create Profile");
        b_CreateProfile.setStyle("-fx-border-color: #0078D7; -fx-background-radius: 5; -fx-padding: 5 10;");
        b_CreateProfile.setOnAction(e -> {
            if (profileWindow == null) {
                profileWindow = createProfile();
            }
            if (profileWindow.isShowing())
                profileWindow.hide();
            else
                profileWindow.show(primaryStage);
        });
        
        Button b_requestReviewer = new Button("Request to Become Reviewer");
        b_requestReviewer.setStyle("-fx-border-color: #0078D7; -fx-background-radius: 5; -fx-padding: 5 10;");
        b_requestReviewer.setOnAction(e -> {
            if (currentUser.getRole().equals("student") || currentUser.getRole().equals("Student")) {
            try { boolean alreadyRequested = trustedController.isReviewerRequestPending(currentUser.getUsername());
                  if (!alreadyRequested) {
                	  trustedController.requestToBecomeReviewer(currentUser.getUsername());
                      Alert alert = new Alert(Alert.AlertType.INFORMATION, "Request sent to become a reviewer.");
                      alert.showAndWait();
                    } 
                  else {
                	  Alert alert = new Alert(Alert.AlertType.WARNING, "You have already requested to become a reviewer.");
                      alert.showAndWait();
                    }
             } 
            catch (ValidationException e1) {
                    e1.printStackTrace();
            }
          }
        });
        
        Button b_viewTrustedReviewers = new Button("View Trusted Reviewers");
        b_viewTrustedReviewers.setStyle("-fx-border-color: #0078D7; -fx-background-radius: 5; -fx-padding: 5 10;");
        b_viewTrustedReviewers.setOnAction(e -> {
            displayTrustedReviewers();  
        });
        
        Button b_viewRequests = new Button("View Pending Reviewer Requests");
        b_viewRequests.setStyle("-fx-border-color: #0078D7; -fx-background-radius: 5; -fx-padding: 5 10;");
        b_viewRequests.setOnAction(e -> {
            InstructorController instructorController = new InstructorController(currentUser);
            instructorController.loadPendingRequests();
            instructorController.showInstructorRequests(primaryStage); 
        });
        
        Button b_flaggedButton = new Button("View Flagged Content");
        b_flaggedButton.setStyle("-fx-border-color: red; -fx-background-radius: 5; -fx-padding: 5 10;");
        b_flaggedButton.setOnAction(e -> displayFlaggedContent());
        
        Button b_viewMyFeedback = new Button("View My Feedback");
        b_viewMyFeedback.setStyle("-fx-border-color: #0078D7; -fx-background-radius: 5; -fx-padding: 5 10;");
        b_viewMyFeedback.setOnAction(e -> displayMyFeedback());
        
        if (currentUser.getRole().equals("Admin"))
        {
        	topPane.getChildren().addAll(b_ViewProfile, b_CreateProfile, b_ViewInstructorsRequests, b_MessageSomeone, b_ViewMyMessages);
        }
        
        if (currentUser.getRole().equals("Student")) 
        {
            topPane.getChildren().addAll(b_AddQuestion, b_ViewProfile, b_CreateProfile, b_requestReviewer, b_viewTrustedReviewers);
        }
        
        if (currentUser.getRole().equals("Staff")) 
        {
            topPane.getChildren().addAll(b_ViewProfile, b_CreateProfile, b_ViewAllPrivateFeedback, b_ViewInstructorsRequests, b_MessageSomeone, b_ViewMyMessages, b_flaggedButton);
        }
        
        if (currentUser.getRole().equals("Instructor")) {
            	topPane.getChildren().addAll(b_viewRequests, b_CreateProfile, b_ViewAllPrivateFeedback, b_InstructorsRequest, b_MessageSomeone, b_ViewMyMessages);
        }
        
        if (currentUser.getRole().equals("Reviewer"))
        {
        	topPane.getChildren().addAll(b_CreateProfile, b_ViewProfile, b_viewMyFeedback);
        }

        primaryStage.setScene(new Scene(border, 900, 600));
        primaryStage.show();

        List<Question> existingQuestions = questionController.getAllQuestions();
        int questionNumber = 1;
        for (Question q : existingQuestions) {
            renderQuestion(q, questionNumber++);
        }
    }

    /**
	* Creates a question window pop up.
	*
	* @return window the question window
	* 
	*/
    private Popup showQuestionWindow() {
        Popup window = new Popup();
        BorderPane questionBox = new BorderPane();

        ComboBox<String> topicsInput = new ComboBox<>();
        Label topicsLabel = new Label("Topics: ");
        HBox topicBox = new HBox(topicsLabel, topicsInput);
        topicsInput.getItems().addAll(topicsList);
        questionBox.setTop(topicBox);

        TextArea questionInput = new TextArea();
        BorderPane.setMargin(questionInput, new Insets(20));
        questionInput.setWrapText(true);
        questionInput.setStyle("-fx-font-size: 15px;");
        questionBox.setCenter(questionInput);

        Button submitButton = new Button("Submit");
        questionBox.setBottom(submitButton);

        submitButton.setOnAction(e -> {
            Question q = new Question(0, questionInput.getText().trim(), currentUser.getUsername());
            try {
                questionController.createQuestion(q);
                renderQuestion(q, questionPane.getChildren().size() + 1);
                topicsInput.setValue(null);
                questionInput.clear();
                questionWindow.hide();
            } catch (ValidationException e1) {
                e1.printStackTrace();
            }
        });

        questionBox.setStyle(Styles.questionBGColor);
        questionBox.setPrefWidth(400);
        questionBox.setPrefHeight(300);
        window.centerOnScreen();
        window.getContent().add(questionBox);
        return window;
    }
    
    /**
   	* Displays a window to reopen already completed instructor requests.
   	* 
   	*/
    private void reopenInstructorRequests() throws SQLException
    {
    	Stage popupStage = new Stage();
    	
    	VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        
        BorderPane requestBox = new BorderPane();
    	
    	List<InstructorRequest> requests = InstructorRequests.getCompletedInstructorRequests();
    	if (requests.isEmpty())
    	{
    		Label empty = new Label("There are no completed requests to reopen.");
    		empty.setStyle("-fx-font-size: 15px;");
    		HBox emptyBox = new HBox(10, empty);
    		
    		requestBox.setCenter(emptyBox);
            layout.getChildren().add(requestBox);
            popupStage.setScene(new Scene(layout, 400, 250));
            popupStage.show();
    	}
    	else
    	{
    	  for (InstructorRequest r : requests) {
              Label request = new Label("Completed Request for " + r.getToAdmin());
              Label request2 = new Label("\n" + r.getText());
              request.setStyle("-fx-font-size: 17px;-fx-font-weight: bold;");
              request2.setStyle("-fx-font-size: 15px;");
              
              Button b_reopen = new Button("Reopen");
              Button b_done = new Button("Done");
              
              HBox reviewBox = new HBox(10, request, request2, b_reopen);
              
              b_reopen.setOnAction(e->{
            	  InstructorRequests.updateInstructorRequestStatus(r.fromInstructor, r.toAdmin, "incomplete", r.text);
            	  Label success = new Label("Request Reopened.");
                  success.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
                  reviewBox.getChildren().addAll(success);
              });
              
              b_done.setOnAction(e->{
            	  popupStage.close();
              });
              
              requestBox.setCenter(reviewBox);
              requestBox.setBottom(b_done);
              layout.getChildren().add(requestBox);
              popupStage.setScene(new Scene(layout, 400, 250));
              popupStage.show();
    	  }
    	}
    }
    
    /**
	* Creates a popup for creating instructor requests.
	*
	* @return window the popup
	* 
	*/
    private Popup createInstructorRequest()
    {
    	Popup window = new Popup();
    	BorderPane requestBox = new BorderPane();
    	
    	ComboBox<String> adminInput = new ComboBox<>();
        Label adminLabel = new Label("Choose admin to request: ");
        HBox adminBox = new HBox(adminLabel, adminInput);
        adminInput.getItems().addAll(adminsList);
        requestBox.setRight(adminBox);
        
        TextArea requestInput = new TextArea();
        BorderPane.setMargin(requestInput, new Insets(20));
        requestInput.setWrapText(true);
        requestInput.setStyle("-fx-font-size: 15px;");
        requestBox.setCenter(requestInput);

        Button b_reopenRequest = new Button("Re-Open a Completed Request");
        b_reopenRequest.setOnAction(e-> {
        	try {
				reopenInstructorRequests();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
        });
        Button submitButton = new Button("Submit request");
        requestBox.setTop(b_reopenRequest);
        requestBox.setBottom(submitButton);
        
        submitButton.setOnAction(e -> {
           try {
			instructorRequestsController.createInstructorRequest(currentUser.getUsername(), adminInput.getValue(), requestInput.getText(), "incomplete");
		} catch (ValidationException e1) {
			e1.printStackTrace();
		}
                requestInput.clear();
                instructorRequestWindow.hide();
          
        });

        requestBox.setStyle(Styles.questionBGColor);
        requestBox.setPrefWidth(400);
        requestBox.setPrefHeight(300);
        window.centerOnScreen();
        window.getContent().add(requestBox);
        return window;
    	
    }
    
    /**
	* Creates a popupStage for updating instructor requests.
	*
	* @param request the instructor request to be updated
	* 
	*/
    private void updateInstructorRequest(InstructorRequest request)
    {
    	 Stage popupStage = new Stage();
    	
    	 VBox layout = new VBox(10);
         layout.setPadding(new Insets(20));
    	
    	BorderPane requestBox = new BorderPane();
    	
    	ComboBox<String> statusInput = new ComboBox<>();
        statusInput.getItems().addAll(statusList);
        
        Button b_done = new Button("Done");
        b_done.setOnAction(e-> {InstructorRequests.updateInstructorRequestStatus(request.fromInstructor, request.toAdmin, statusInput.getValue(), request.getText());
        popupStage.close();
        });
        
        requestBox.setTop(new HBox(new Label("Update Completion Status")));
        requestBox.setCenter(statusInput);
        requestBox.setBottom(b_done);
        
        layout.getChildren().add(requestBox);
        popupStage.setScene(new Scene(layout, 400, 250));
        popupStage.show();
    }
    
    /**
	* Creates a popup for creating messages between instructors, admin and staff.
	*
	* @return window the popup
	* 
	*/
    private Popup createMessage()
    {
    	Popup window = new Popup();
    	BorderPane messageBox = new BorderPane();
    	
    	TextField recipient = new TextField();
    	recipient.setPromptText("Enter message recipient");
    	
    	TextField message = new TextField();
    	message.setPromptText("Enter your message");

        Button submitButton = new Button("Send message");
        messageBox.setBottom(submitButton);
        
        submitButton.setOnAction(e -> {
           try {
        	Message m = new Message(message.getText().trim(), currentUser.getUsername(), recipient.getText());
			messagesController.createMessage(m);
		} catch (ValidationException e1) {
			e1.printStackTrace();
		}
                createMessageWindow.hide();
          
        });

        VBox layout = new VBox(10, new Label("Message Someone"), recipient, message, submitButton);
        layout.setStyle(Styles.questionBGColor);
        layout.setPrefWidth(400);
        layout.setPrefHeight(300);
        window.centerOnScreen();
        window.getContent().add(layout);
        return window;
    }
    
    /**
	* Creates a popup for creating a profile.
	*
	* @return pop the popup
	*/
    private Popup createProfile() 
    {
    	Popup pop = new Popup();
    	BorderPane infoBox = new BorderPane();
    	
       	ComboBox<String> selectRole = new ComboBox<>();
        Label rolesLabel = new Label("Select your role: ");
        HBox rolesBox = new HBox(rolesLabel, selectRole);
        selectRole.getItems().addAll(rolesList);
        infoBox.setTop(rolesBox);

        TextField bioField = new TextField();
        bioField.setPromptText("Enter your bio");
        
        TextField displayNameField = new TextField();
        displayNameField.setPromptText("Enter your display name");
    	
        Button submitButton = new Button("Create new profile");
        infoBox.setBottom(submitButton);
        
        submitButton.setOnAction(e -> {
        	currentUser.setHasProfile("true");
        	currentUser.setBiography(bioField.getText().trim());
        	currentUser.setDisplayName(displayNameField.getText().trim());
			System.out.print(currentUser.getHasProfile());
            profileWindow.hide();
            
        });
        
        VBox layout = new VBox(10, new Label("Create Profile"), bioField, displayNameField, submitButton);
        layout.setStyle(Styles.questionBGColor);
        layout.setPrefWidth(400);
        layout.setPrefHeight(300);
        pop.centerOnScreen();
        pop.getContent().add(layout);
        return pop;
    }

    /**
	* Renders the question to the scene
	*
	* @param q the question to be displayed
	* @param number the number of questionPane's children + 1
	* 
	*/
    private void renderQuestion(Question q, int number) {
        HBox questionWrapper = new HBox(10);
        Label indexLabel = new Label(number + ".");
        indexLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        BorderPane questionBox = createQuestionPane(q);
        questionBox.setStyle("-fx-background-color: #e0f7fa; -fx-padding: 10; -fx-background-radius: 10;");

        questionWrapper.getChildren().addAll(indexLabel, questionBox);
        questionWrapper.setUserData(q);
        questionWrapper.setOnMouseClicked(ev -> border.setRight(createAnswer(q)));
        questionPane.getChildren().add(questionWrapper);
    }

    /**
	* Creates an answer BorderPane, including review functionalities related 
	* to the answer.
	*
	* @param q the question to create the answer for
	* @return answerPane the answer BorderPane
	*/
    private BorderPane createAnswer(Question q) {
        BorderPane answerPane = new BorderPane();
        VBox answerBox = new VBox(10);
        Pane questionBox = createQuestionPane(q);

        Button replyButton = new Button("Reply");
        TextField replyInput = new TextField();
        HBox replyBox = new HBox(10, replyButton, replyInput);

        answerPane.setUserData(q);
        answerBox.setMaxWidth(600);
        questionBox.setStyle(Styles.questionBGColor);

        for (Answer a : answersController.getAnswersForQuestion(q.getId())) {
            VBox answerCard = new VBox(8);
            answerCard.setStyle("-fx-background-color: #ffffff; -fx-border-color: lightgray; -fx-padding: 12; -fx-background-radius: 8;");
            answerCard.getChildren().add(a.label);
            
            if (currentUser.getRole().equals("Staff"))
            {
            	Button b_delete = new Button("Delete Answer");
            	b_delete.setStyle("-fx-background-color: red; -fx-text-fill: white;");
            	b_delete.setOnAction(e-> {
        	        answersController.deleteAnswer(a.getId());
        	        answerBox.getChildren().remove(answerCard);
        	    });
            	answerCard.getChildren().add(b_delete);
            }
            
            if (currentUser.getRole().equals("Reviewer")) 
            {
                Button addRev = new Button("Leave Review");
                addRev.setOnAction(evt -> showCreateReviewPopup(a.getId()));
                answerCard.getChildren().add(addRev);
            }

            List<Review> answerReviews = reviews.getReviewsForAnswer(a.getId());
            if (!answerReviews.isEmpty()) {
                Label header = new Label("Reviews:");
                header.setStyle("-fx-font-weight: bold");
                answerCard.getChildren().add(header);

                for (Review review : answerReviews) {
                    VBox reviewDetails = new VBox(5);
                    Label r = new Label("- " + review.getText() + " (weight: " + review.getWeightage() + ")");
                    Label reviewerLabel = new Label("by: " + review.getReviewerId());
                    HBox reviewBox = new HBox(10, r);
                    
                    Button sendFeedback = new Button ("Send private feedback");
                    sendFeedback.setOnAction(ev -> {
                        showFeedbackPopup(review.getReviewerId()); 
                    });
                    reviewBox.getChildren().add(sendFeedback);
                    
                    if (currentUser.getRole().equals("Student") && !trustedList.contains(review.getReviewerId())) {
                        Button trustBtn = new Button("☆ Trust This Reviewer");
                        trustBtn.setOnAction(ev -> {
                            trustedController.trustReviewer(currentUser.getUsername(), review.getReviewerId());
                            trustedList.add(review.getReviewerId());
                            trustBtn.setDisable(true);
                            trustBtn.setText("⭐ Trusted");
                        });
                        reviewBox.getChildren().add(trustBtn);
                    } 
                    else if (trustedList.contains(review.getReviewerId())) {
                        Label trusted = new Label("⭐ Trusted");
                        trusted.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
                        reviewBox.getChildren().add(trusted);
                    }
                    
                    reviewDetails.getChildren().addAll(reviewBox, reviewerLabel);
                    answerCard.getChildren().add(reviewDetails);
                    
                    if (currentUser.getRole().equals("Reviewer")) {
                        Button updateBtn = new Button("Update Review");
                        updateBtn.setOnAction(evt -> showUpdateReviewPopup(review));
                        answerCard.getChildren().add(updateBtn);
                    }
                }
            }
            answerBox.getChildren().add(answerCard);
        }
        replyButton.setOnAction(e -> {
            try {
            	if (currentUser.getRole().equals("Staff"))
            	{
            		String staffUsername = currentUser.getUsername() + " (" + currentUser.getRole() +  ") ";
            		 Answer a = new Answer(answerIndex++, q.getId(), replyInput.getText().trim(), staffUsername);
            		 answersController.createAnswer(a);
                     a.createLabel();
                     answerBox.getChildren().add(a.label);
            	}
            	else
            	{
            		Answer a = new Answer(answerIndex++, q.getId(), replyInput.getText().trim(), currentUser.getUsername());
            		answersController.createAnswer(a);
            		a.createLabel();
            		answerBox.getChildren().add(a.label);
            	}
            } 
            catch (ValidationException e1) {
                System.out.println(e1.toString());
            }
        });
        answerPane.setTop(questionBox);
        answerPane.setCenter(answerBox);
        answerPane.setBottom(replyBox);
        BorderPane.setMargin(replyBox, new Insets(20));
        return answerPane;
    }

    /**
	* Displays a popup for creating a review.
	* 
	*/
    private void showCreateReviewPopup(int answerId)
    {
    	  Stage popupStage = new Stage();
          popupStage.setTitle("Create Review");

          VBox layout = new VBox(10);
          layout.setPadding(new Insets(20));
          
          Label errorLabel = new Label();
          errorLabel.setStyle("-fx-text-fill: red");

          TextArea reviewInput = new TextArea();
          reviewInput.setWrapText(true);

          Button submit = new Button("Submit Review");
          submit.setOnAction(e -> {
              try {
                  reviews.createReview(answerId, currentUser.getUsername(), reviewInput.getText().trim(), 0, null);
                  System.out.print("Review created for " + currentUser.getUsername() + " with text " + reviewInput.getText() + "with answerId " + answerId);
                  popupStage.close();
              } catch (ValidationException ve) {
            	  errorLabel.setText(ve.getMessage());
              }
          });
          layout.getChildren().addAll(new Label("Add a review:"), reviewInput, errorLabel, submit);
          popupStage.setScene(new Scene(layout, 400, 250));
          popupStage.show();
    }
    /**
	* Creates a pop up to update a review.
	*
	* @param originalReview the original review to be updated
	* 
	*/
    private void showUpdateReviewPopup(Review originalReview) {
        Stage popupStage = new Stage();
        popupStage.setTitle("Update Review");

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));

        TextArea reviewInput = new TextArea(originalReview.getText());
        reviewInput.setWrapText(true);

        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red");

        Button submit = new Button("Update Review");
        submit.setOnAction(e -> {
            try {
                reviews.updateReview(originalReview.getId(), reviewInput.getText().trim(), originalReview.getWeightage());
                popupStage.close();
            } catch (ValidationException ve) {
                errorLabel.setText(ve.getMessage());
            }
        });

        layout.getChildren().addAll(new Label("Update your review:"), reviewInput, errorLabel, submit);
        popupStage.setScene(new Scene(layout, 400, 250));
        popupStage.show();
    }

    /**
	* Creates a question BorderPane.
	* 
	* @param question the question object
	* @return pane the question BorderPane
	* 
	*/
    private BorderPane createQuestionPane(Question question) {
        BorderPane pane = new BorderPane();
        VBox texts = new VBox(5);

        Text user = new Text(question.getStudentId());
        user.setStyle("-fx-font-weight: bold");

        Text contents = new Text(question.getText());
        contents.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        HBox topics = new HBox();
        for (String k : question.getKeywords()) {
            topics.getChildren().add(new Label(k));
        }

        texts.getChildren().addAll(user, contents, topics);
        pane.setCenter(texts);
        pane.setUserData(question);
        pane.setMinWidth(250);
        return pane;
    }
    
    /**
	* Displays the list of reviewers that the current student has trusted. 
	* 
	*/
    private void displayTrustedReviewers() {
        VBox trustedReviewersBox = new VBox(10);
        trustedReviewersBox.setPadding(new Insets(10));
        trustedReviewersBox.setStyle("-fx-background-color: #f4f4f4; -fx-border-radius: 10; -fx-padding: 10;");

        Label titleLabel = new Label("Trusted Reviewers:");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        trustedReviewersBox.getChildren().add(titleLabel);

        if (trustedList.isEmpty()) {
            Label noReviewersLabel = new Label("You have no trusted reviewers.");
            trustedReviewersBox.getChildren().add(noReviewersLabel);
        } else {
  
            for (String reviewer : trustedList) {
                Label reviewerLabel = new Label(reviewer);
                trustedReviewersBox.getChildren().add(reviewerLabel);
            }
        }
        border.setRight(trustedReviewersBox);
    }
    
    /**
	* Displays a user's profile that they have created.
     * @throws SQLException 
	* 
	*/
    private void displayProfile() throws SQLException
    {
    	VBox profile = new VBox(30);
    	profile.setPadding(new Insets(30));
    	profile.setStyle("-fx-background-color: #f4f4f4; -fx-border-radius: 30; -fx-padding: 30;");
    	
    	String displayUsername = currentUser.getUsername() + "'s Profile";
    	Label title = new Label(displayUsername);
    	title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");
    	profile.getChildren().add(title);
    
    	Label displayName = new Label(currentUser.getDisplayName() + " (" + currentUser.getRole() + ")");
    	profile.getChildren().add(displayName);
    	displayName.setStyle("-fx-font-size: 15px; -fx-font-weight: bold;");
    	
    	Label bio = new Label(currentUser.getBiography());
    	profile.getChildren().add(bio);
    	
    	if (currentUser.getRole().equals("Reviewer"))
    	{
    		 List<Review> profileReviews = reviews.getReviewsforReviewer(currentUser.getUsername());
    		 Label reviewsTitle = new Label("My Reviews");
    		 reviewsTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
    		 profile.getChildren().addAll(reviewsTitle);
    		 
    	        for (Review r : profileReviews) {
    	        	Label review = new Label(r.getText());
    	        	 review.setStyle("-fx-font-size: 15px");
    	            profile.getChildren().addAll(review);
    	        }
    	        
    	     List<PrivateFeedback> privFeedback = feedbackController.getFeedbackForReviewer(currentUser.getUsername());
    	     Label privFeedbackTitle = new Label("My Feedback");
    	     privFeedbackTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
    	     profile.getChildren().addAll(privFeedbackTitle);
    	     
    	     	for (PrivateFeedback p : privFeedback)
    	     	{
    	     		Label feedback = new Label("From: " + p.getFromStudent() + "\n	" + p.getFeedbackText());
    	     		feedback.setStyle("-fx-font-size: 15px");
    	     		profile.getChildren().addAll(feedback);				
    	     	}
    	}
    	
    	border.setRight(profile);

    }
    
    /**
	* Displays all of the instructor's requests to admin. Staff, instructors, 
	* and admin can access this list.
	* 
	*/
    private void displayInstructorRequests()
    {
    	VBox instructorRequests = new VBox(30);
    	instructorRequests.setPadding(new Insets(30));
    	instructorRequests.setStyle("-fx-background-color: #f4f4f4; -fx-border-radius: 30; -fx-padding: 30;");
    	
    	Label titleLabel = new Label("Instructor's Requests:");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        instructorRequests.getChildren().add(titleLabel);
        
        if (currentUser.getRole().equals("Staff") || currentUser.getRole().equals("Instructor"))
        {
        	List<InstructorRequest> iRequests = instructorRequestsController.getAllInstructorRequests();
        	for (InstructorRequest request : iRequests) {
        			Label iRequest = new Label("For " + request.getToAdmin() + "\nFrom: " + request.getFromInstructor() + "\n	" + request.getText());
        			iRequest.setStyle("-fx-font-size: 15px");
        			instructorRequests.getChildren().addAll(iRequest);
        			if (request.getStatus().equals("complete"))
        			{
        				Label completeLabel = new Label(" (completed) ");
        				completeLabel.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
        				instructorRequests.getChildren().addAll(completeLabel);
        			}
        	}
        }
        else if (currentUser.getRole().equals("Admin"))
        {
        	try {
				List<InstructorRequest> adminsRequests = instructorRequestsController.getInstructorRequests(currentUser.getUsername());
				for (InstructorRequest request : adminsRequests) {
	        			Label adminsRequest = new Label("From " + request.getFromInstructor() + ": \n	" + request.getText());
	        			adminsRequest.setStyle("-fx-font-size: 15px");
	        			instructorRequests.getChildren().addAll(adminsRequest);
	        		
	        			Button b_updateRequestStatus = new Button("Update My Progress on This Request");
	        			b_updateRequestStatus.setStyle("-fx-border-color: #0078D7; -fx-background-radius: 5; -fx-padding: 5 10;");
	        			b_updateRequestStatus.setOnAction(e -> {
	        				updateInstructorRequest(request);
	        			});
	        			instructorRequests.getChildren().addAll(b_updateRequestStatus);
	        			
	        			if (request.getStatus().equals("complete"))
	        			{
	        				Label completeLabel = new Label(" (completed) ");
	        				completeLabel.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
	        				instructorRequests.getChildren().addAll(completeLabel);
	        			}
				}
			} catch (ValidationException e) {
				e.printStackTrace();
			}
        }
        
        border.setRight(instructorRequests);
    }
    
    /**
   	* Displays a reviewer's private feedback to them.
   	* 
   	*/
    private void displayMyFeedback()
    {
    	VBox myFeedback = new VBox(30);
    	myFeedback.setPadding(new Insets(30));
    	myFeedback.setStyle("-fx-background-color: #f4f4f4; -fx-border-radius: 30; -fx-padding: 30;");
    	
    	Label titleLabel = new Label("My Private Feedback");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        myFeedback.getChildren().add(titleLabel);
        
        List<PrivateFeedback> p = feedbackController.getFeedbackForReviewer(currentUser.getUsername());
        for (PrivateFeedback f : p) {
    	Label fLabel = new Label("From: " + f.getFromStudent() + "\n	" + f.getFeedbackText());
        	 fLabel.setStyle("-fx-font-size: 15px");
             myFeedback.getChildren().addAll(fLabel);
        }
        
        border.setRight(myFeedback);
    	
    }
    
    /**
	* Displays all the private feedback between students and reviewers.
	* 
	*/
    private void displayAllFeedback()
    {
    	VBox allFeedback = new VBox(30);
    	allFeedback.setPadding(new Insets(30));
    	allFeedback.setStyle("-fx-background-color: #f4f4f4; -fx-border-radius: 30; -fx-padding: 30;");
    	
    	Label titleLabel = new Label("All Private Feedback");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        allFeedback.getChildren().add(titleLabel);
        
        List<PrivateFeedback> pList = feedbackController.getAllFeedback();
        for (PrivateFeedback feedback : pList) {
        	Label feedbackLabel = new Label("From: " + feedback.getFromStudent() + "\nTo: " + feedback.getToReviewer() + "\n	" + feedback.getFeedbackText());
        	 feedbackLabel.setStyle("-fx-font-size: 15px");
             allFeedback.getChildren().addAll(feedbackLabel);
        }
        border.setRight(allFeedback);
    }
    
    /**
   	* Creates a pop up to create feedback
   	*
   	* @param reviewerId the reviewer to send the feedback to
   	* 
   	*/
    private void showFeedbackPopup(String reviewerId) {
        Stage popup = new Stage();
        popup.setTitle("Send Private Feedback to " + reviewerId);

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));

        TextArea feedbackArea = new TextArea();
        feedbackArea.setPromptText("Write your feedback here...");
        Label message = new Label();

        Button send = new Button("Send");
        send.setOnAction(e -> {
            String feedback = feedbackArea.getText().trim();
            if (!feedback.isEmpty()) {
                feedbackController.submitFeedback(reviewerId, currentUser.getUsername(), feedback);
                message.setText("Feedback sent!");
                feedbackArea.clear();
                popup.hide();
            } else {
                message.setText("Cannot send empty feedback.");
            }
        });

        layout.getChildren().addAll(new Label("Feedback:"), feedbackArea, send, message);
        popup.setScene(new Scene(layout, 400, 250));
        popup.show();
    }
    
    /**
	* Displays the current user's messages.
	* 
	*/
    public void displayMyMessages()
    {
    	VBox myMessageBox = new VBox(30);
    	myMessageBox.setPadding(new Insets(30));
    	myMessageBox.setStyle("-fx-background-color: #f4f4f4; -fx-border-radius: 30; -fx-padding: 30;");
    	
    	Label titleLabel = new Label("My Messages");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        myMessageBox.getChildren().add(titleLabel);
        
        List<Message> mList = messagesController.getMessagesByRecipient(currentUser.getUsername());
        for (Message m : mList) {
        	System.out.print("From: " + m.getFromUsername() + "\nTo: " + m.getToUsername() + "\n	" + m.getText());
        	Label messageLabel = new Label("From: " + m.getFromUsername() + "\n		" + m.getText());
        	 messageLabel.setStyle("-fx-font-size: 15px");
             myMessageBox.getChildren().addAll(messageLabel);
        }
        border.setRight(myMessageBox);
    }
    
    /**
	* Displays all of the messages in the system.
	* 
	*/
    public void displayAllMessages()
    {
    	VBox MessageBox = new VBox(30);
    	MessageBox.setPadding(new Insets(30));
    	MessageBox.setStyle("-fx-background-color: #f4f4f4; -fx-border-radius: 30; -fx-padding: 30;");
    	
    	Label titleLabel = new Label("All Messages");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        MessageBox.getChildren().add(titleLabel);
        
        List<Message> list = messagesController.getAllMessages();
        
        for (Message m : list) {
        	System.out.print("From: " + m.getFromUsername() + "\nTo: " + m.getToUsername() + "\n	" + m.getText());
        	Label messageLabel = new Label("From: " + m.getFromUsername() + "\nTo: " + m.getToUsername() + "\n	" + m.getText());
        	 messageLabel.setStyle("-fx-font-size: 15px");
             MessageBox.getChildren().addAll(messageLabel);
        }
        border.setRight(MessageBox);
    }
    
    /**
   	* Displays the flagged content in the system and allows the staff to update the flagged words in the UI.
   	* 
   	*/
    public void displayFlaggedContent() {
        VBox flaggedBox = new VBox(15);
        flaggedBox.setPadding(new Insets(15));
        flaggedBox.setStyle("-fx-background-color: #fff3f3;");

        Label title = new Label("Flagged Content");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: red;");
        flaggedBox.getChildren().add(title);

        // Display flagged content
        for (Question q : questionController.getAllQuestions()) {
            for (String word : flaggedWords) {
                if (q.getText().toLowerCase().contains(word)) {
                    VBox entry = new VBox(3);
                    entry.setStyle("-fx-background-color: #ffeaea; -fx-padding: 8; -fx-background-radius: 8;");
                    Label content = new Label("[Q] " + q.getText());
                    content.setOnMouseClicked(ev -> border.setRight(createAnswer(q)));
                    entry.getChildren().addAll(content, new Label("By: " + q.getStudentId()));
                    flaggedBox.getChildren().add(entry);
                    break;
                }
            }
        }

        for (Answer a : answersController.getAllAnswers()) {
            for (String word : flaggedWords) {
                if (a.getText().toLowerCase().contains(word)) {
                    VBox entry = new VBox(3);
                    entry.setStyle("-fx-background-color: #ffeaea; -fx-padding: 8; -fx-background-radius: 8;");
                    Label content = new Label("[A] " + a.getText());
                    entry.getChildren().addAll(content, new Label("By: " + a.getStudentId()));
                    flaggedBox.getChildren().add(entry);
                    break;
                }
            }
        }

        // Add UI for editing flagged words
        Label editTitle = new Label("Edit Flagged Words");
        editTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: blue;");
        flaggedBox.getChildren().add(editTitle);

        ListView<String> wordListView = new ListView<>();
        wordListView.getItems().addAll(flaggedWords);
        flaggedBox.getChildren().add(wordListView);

        TextField newWordField = new TextField();
        newWordField.setPromptText("Add new word");
        newWordField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                String newWord = newWordField.getText().trim().toLowerCase();
                if (!newWord.isEmpty() && !flaggedWords.contains(newWord)) {
                    flaggedWords.add(newWord);
                    wordListView.getItems().add(newWord);
                    newWordField.clear();
                }
            }
        });
        flaggedBox.getChildren().add(newWordField);

        Button removeButton = new Button("Remove Selected");
        removeButton.setOnAction(event -> {
            String selectedWord = wordListView.getSelectionModel().getSelectedItem();
            if (selectedWord != null) {
                flaggedWords.remove(selectedWord);
                wordListView.getItems().remove(selectedWord);
            }
        });
        flaggedBox.getChildren().add(removeButton);

        ScrollPane scroll = new ScrollPane(flaggedBox);
        scroll.setFitToWidth(true);
        border.setRight(scroll);
    }
}