import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import javafx.scene.input.MouseEvent;

public class BrickBreakerView
{
    private Stage primaryStage;
    private Label label;
    private Label pointsLabel;
    private Label level;
    private GridPane gridPane;
    private VBox root;
    private Button startButton;
    private Button exitButton;
    private Button leaderBoard;
    private boolean chronologicalOrderSelected;
    private boolean highestScoreOrderSelected;
    private Alert alert;
    private EventHandler<ActionEvent> startButtonHandler;
    private EventHandler<MouseEvent> brickClickHandler;
    private EventHandler<ActionEvent> leaderBoardHandler;
    private EventHandler<ActionEvent> perTimeButtonHandler;
    private EventHandler<ActionEvent> perScoreButtonHandler;

   public BrickBreakerView()
   {
        //default contructor
   }
   
   //Setting the view of the start screen
   public void setView(Stage primaryStage)
    { 
        this.primaryStage = primaryStage;

        startButton = new Button("Start Game");
        exitButton = new Button("Exit");
        leaderBoard = new Button("Leaderboard");
        label = new Label("BrickBreaker Game");
        label.setFont(Font.font("Arial", FontWeight.BOLD, 35));

        root = new VBox();
        root.setAlignment(Pos.CENTER);
        root.setSpacing(20);
        root.setPadding(new Insets(10));
        root.getChildren().addAll(label, startButton, exitButton, leaderBoard);
        root.setPrefWidth(800);
        root.setPrefHeight(800);

        Scene scene = new Scene (root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Brick Breaker");
        primaryStage.show();

        startButton.setOnAction(event -> 
        {
            if (startButtonHandler != null) 
            {
                startButtonHandler.handle(event);
            }
        });

        leaderBoard.setOnAction(event -> 
        {
            if (leaderBoardHandler != null) 
            {
                leaderBoardHandler.handle(event);
            }
        });
        
        exitButton.setOnAction(event -> primaryStage.close());
    }

    //Creates the grid pane for the game
    public void createGridPane(int rows, int columns, Brick[][] bricks)
    {
        pointsLabel = new Label("TOTAL POINTS: 0");
        pointsLabel.setFont(Font.font("Arial", FontWeight.BOLD,FontPosture.REGULAR, 20));
        pointsLabel.setTextFill(Color.PURPLE);
        pointsLabel.setTranslateY(-150);

        level = new Label("LEVEL ");
        level.setFont(Font.font("Arial", FontWeight.BOLD, FontPosture.REGULAR, 30));
        level.setTextFill(Color.HOTPINK);
        level.setTranslateY(-160);

        gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setPadding((new Insets(10)));
        gridPane.setHgap(0);
        gridPane.setVgap(0);

        VBox.setMargin(gridPane, new Insets(0, 0, 150, 0)); //Centering the grid

        for ( int row = 0; row < rows; row++)
        {
            for(int col = 0; col < columns; col++)
            {
                Brick brick = bricks[row][col];
                if(brick instanceof SpecialBricks)
                {
                    Image image = ((SpecialBricks) brick).getImage();
                    ImageView imageview = new ImageView(image);
                    imageview.setFitWidth(40);
                    imageview.setFitHeight(40);
                    gridPane.add(imageview, col, row);

                    imageview.setOnMouseClicked(event ->
                    {
                        if (brickClickHandler != null) 
                        {
                            brickClickHandler.handle(event);
                        }
                    });
                }
                else
                {
                    Rectangle rectangle = new Rectangle(45, 45);
                    rectangle.setFill(brick.getColor());
                    // Set the stroke color and width for distinct edges
                    rectangle.setStroke(Color.BLACK);
                    rectangle.setStrokeWidth(2);
                    gridPane.add(rectangle, col, row);

                    rectangle.setOnMouseClicked(event ->
                    {
                        if (brickClickHandler != null) 
                        {
                            brickClickHandler.handle(event);
                        }
                    });
                }                
            }
        }
        root.getChildren().addAll(gridPane,pointsLabel,level);
    }

    //Updates the grid with the correct bricks
    public void updateGrid(Brick[][] bricks, int rows, int columns)
    {
        //Clear of the gameGrid
        gridPane.getChildren().clear();

        for(int row = 0; row < rows; row++)
        {
            for(int col = 0; col < columns; col++)
            {
                Brick brick = bricks[row][col];
                // Check if the brick is a normal brick or an image brick
                if(brick instanceof SpecialBricks)
                {
                    // If it's an image brick, create an ImageView with the specified image
                    Image image = ((SpecialBricks) brick).getImage();
                    ImageView imageview = new ImageView(image);
                    imageview.setFitWidth(40);
                    imageview.setFitHeight(40);

                    // Set the row and column indices for the ImageView
                    GridPane.setRowIndex(imageview, row);
                    GridPane.setColumnIndex(imageview, col);

                    imageview.setOnMouseClicked(brickClickHandler);

                    gridPane.getChildren().add(imageview);
                }
                else
                {
                    Rectangle rectangle = new Rectangle(45,45);
                    rectangle.setFill(brick.getColor());
                    
                    GridPane.setRowIndex(rectangle, row);
                    GridPane.setColumnIndex(rectangle, col);

                    // Set the stroke color and width for distinct edges
                    rectangle.setStroke(Color.BLACK);
                    rectangle.setStrokeWidth(2);

                    rectangle.setOnMouseClicked(brickClickHandler);

                    gridPane.getChildren().add(rectangle);
                }
            }
        }
    }

    //Setter methods
   public void setStartButtonAction(EventHandler<ActionEvent> startButtonHandler) 
    {
        this.startButtonHandler = startButtonHandler;
    }

    public void setBrickClickHandler(EventHandler<MouseEvent> brickClickHandler) 
    {
        this.brickClickHandler = brickClickHandler;
    }

    public void setLeaderBoardAction(EventHandler<ActionEvent> leaderBoardHandler) 
    {
        this.leaderBoardHandler = leaderBoardHandler;
    }

    public void setPerTimeButtonHandler(EventHandler<ActionEvent> perTimeButtonHandler) 
    {
        this.perTimeButtonHandler = perTimeButtonHandler;
    }

    public void setPerScoreButtonHandler(EventHandler<ActionEvent> perScoreButtonHandler) 
    {
        this.perScoreButtonHandler = perScoreButtonHandler;
    }

    public void setChronologicalOrderSelected(boolean value) 
    {
        chronologicalOrderSelected = value;
    }
    
    public void setHighestScoreOrderSelected(boolean value) 
    {
        highestScoreOrderSelected = value;
    }

    //Checks if the chronological order button is selected
    public boolean isChronologicalOrderSelected() 
    {
        return chronologicalOrderSelected;
    }
    
    //Checks if the highest score order button is selected
    public boolean isHighestScoreOrderSelected() 
    {
        return highestScoreOrderSelected;
    }
    
    //Clears the game screen
    public void clearStartScreen()
    {
        startButton.setVisible(false);
        exitButton.setVisible(false);
        label.setVisible(false);
        leaderBoard.setVisible(false);
    }

    //Updates the points label
    public void updatePointsLabel(int score)
    {
        pointsLabel.setText("TOTAL POINTS: " + score);
    }

    //Updates the level label
    public void updateLevelLabel(int lvl)
    {
        level.setText("LEVEL " + lvl);
    }

    //Adds a game over label
    public void addGameOverlabel()
    {
        Label gameOver = new Label("GAME OVER");
        gameOver.setFont(Font.font("Arial", FontWeight.BOLD, FontPosture.REGULAR, 30));
        gameOver.setTextFill(Color.PURPLE);
        gameOver.setTranslateY(-600);

        root.getChildren().addAll(gameOver);
    }

    //method that prompts the player for their name
    public String promptForPlayerName()
    {
        TextInputDialog dialog = new TextInputDialog(null);
        dialog.setTitle("Player Name");
        dialog.setHeaderText(null);
        dialog.setContentText("Enter your name");

        dialog.initOwner(primaryStage);

        String playerName = dialog.showAndWait().orElse("");

        //Check if the player name is empty and handles it accordingly
        if(playerName.isEmpty())
        {
            playerName = "Uknown player";
            return playerName;
        }
        
        return playerName;
    }

    //method that displays the leaderboard
    public void displayScoresTable(TableView<ScoreEntry> tableView) 
    {
        if(alert == null)
        {
            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Score Table");
            alert.setHeaderText(null);

            // Add event listener to reset the alert variable when closed
            alert.setOnHidden(event -> 
            {
                alert = null;
            });
            
            // Create a GridPane to hold the buttons
            GridPane buttonPane = new GridPane();
            buttonPane.setHgap(10);
            buttonPane.setVgap(10);
            buttonPane.setPadding(new Insets(10));

            Button perTimeButton = new Button("Filter Chronologically");
            Button perScoreButton = new Button("Sort by Highest Score");
            
            perTimeButton.setOnAction(event -> 
            {
                if (perTimeButtonHandler != null) 
                {
                    perTimeButtonHandler.handle(event);
                }
            });

            perScoreButton.setOnAction(event -> 
            {
                if (perScoreButtonHandler != null) 
                {
                    perScoreButtonHandler.handle(event);
                }
            });

            // Add the buttons to the buttonPane
            buttonPane.add(perTimeButton, 0, 0);
            buttonPane.add(perScoreButton, 1, 0);
            
            // Create a VBox to hold the table view and button pane
            VBox vbox = new VBox(10);

            vbox.getChildren().addAll(tableView, buttonPane);
            alert.getDialogPane().setContent(vbox);

            alert.showAndWait();
        }
        else
        {
            // Update the TableView table
            TableView<ScoreEntry> newTableView = (TableView<ScoreEntry>) ((VBox) alert.getDialogPane().getContent()).getChildren().get(0);
            newTableView.getItems().setAll(tableView.getItems());
            newTableView.getColumns().setAll(tableView.getColumns());
        }
    }
}