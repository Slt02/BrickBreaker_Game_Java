import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class BrickBreakerController
{
    private BrickBreakerModel model;
    private BrickBreakerView view;
    private DatabaseConnection databaseConnection;
    private ResultSet rs;

    //Controller's constructor
    public BrickBreakerController(BrickBreakerModel model, BrickBreakerView view, Stage primaryStage)
    {
        this.model = model;
        this.view = view;

        String url = "jdbc:mysql://localhost:3306/scoredb?user=root";
        String username = "root";
        String password = "milko.1-2002";

        databaseConnection = new DatabaseConnection(url, username, password);

        view.setStartButtonAction(this::handleStartButton);
        view.setBrickClickHandler(this::handleBrickClick);
        view.setLeaderBoardAction(this::handleLeaderBoardButton);
        view.setPerScoreButtonHandler(this::handlePerScoreButton);
        view.setPerTimeButtonHandler(this::handlePerTimeButton);

        view.setView(primaryStage);
    }

    //Handling the start button
    private void handleStartButton(ActionEvent event) 
    {
        view.clearStartScreen();
        view.createGridPane(model.getRows(), model.getColumns(), model.getGrid());
        view.updateLevelLabel(model.getLevel());
    }
 
    //Handling the Leaderboard button
    private void handleLeaderBoardButton(ActionEvent event)
    {
        try 
        {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/scoredb?user=root", "root", "milko.1-2002");
            Statement stmt = conn.createStatement();
            String query = "SELECT player_name, score, timestamp FROM scores ";

            if (view.isChronologicalOrderSelected()) 
            {
                query += "ORDER BY timestamp ASC";
            } 
            else if (view.isHighestScoreOrderSelected()) 
            {
                query += "ORDER BY score DESC";
            }
            
            rs = stmt.executeQuery(query);

            // Create an instance of TableView
            TableView<ScoreEntry> tableView = new TableView<>();

            // Create table columns for player name, score, timestamp
            TableColumn<ScoreEntry, String> nameColumn = new TableColumn<>("Player Name");
            nameColumn.setCellValueFactory(new PropertyValueFactory<>("playerName"));

            TableColumn<ScoreEntry, Integer> scoreColumn = new TableColumn<>("Score");
            scoreColumn.setCellValueFactory(new PropertyValueFactory<>("score"));

            TableColumn<ScoreEntry, Timestamp> timestampColumn = new TableColumn<>("Timestamp");
            timestampColumn.setCellValueFactory(new PropertyValueFactory<>("timestamp"));

            // Add columns to the table view
            tableView.getColumns().addAll(nameColumn, scoreColumn, timestampColumn);

            // Populate the table view with data from the result set
            while (rs.next()) {
                String playerName = rs.getString("player_name");
                int score = rs.getInt("score");
                Timestamp timestamp = rs.getTimestamp("timestamp");
                ScoreEntry entry = new ScoreEntry(playerName, score, timestamp);
                tableView.getItems().add(entry);
            }

            // Display the scores table
            view.displayScoresTable(tableView);
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
        }
    }

    //Handling every player's click
    private void handleBrickClick(MouseEvent event)
    {
        if(event.getButton() == MouseButton.PRIMARY)
        {
            Node source = (Node) event.getSource();
            Integer row = GridPane.getRowIndex(source);
            Integer col = GridPane.getColumnIndex(source);

            if(row != null && col != null)
            {
                Brick brick = model.getBrick(row, col);
                if(!brick.getColor().equals(Color.TRANSPARENT))
                {
                    if(brick instanceof SpecialBricks)
                    {
                        SpecialBricks specialBrick = (SpecialBricks) brick;
                        model.handleSpecialBrick(row, col, specialBrick);
                    }
                    else
                    {
                        model.handleBricks(row, col);
                    }
                }

                view.updateGrid(model.getGrid(), model.getRows(), model.getColumns());
                view.updatePointsLabel(model.getTotalScore());
                
                if(model.movesExhausted())
                {
                    handleLevels();
                }
            }
        }
    }

    //Handles the per-score button for the sorting to happen
    private void handlePerScoreButton(ActionEvent event)
    {
        view.setChronologicalOrderSelected(false);
        view.setHighestScoreOrderSelected(true);
        
        handleLeaderBoardButton(event);

    }

    //Handles the per-time button for the sorting to happen
    private  void handlePerTimeButton(ActionEvent event)
    {
        view.setHighestScoreOrderSelected(false);
        view.setChronologicalOrderSelected(true);

        handleLeaderBoardButton(event);
    }
    
    //Handles what happens when we pass to the next level or the game is over
    private void handleLevels()
    {
        if(model.GameOver())
        {
            view.addGameOverlabel();
            int score = model.getTotalScore();
            String playerName = view.promptForPlayerName();

            ScoreEntry scoreEntry = new ScoreEntry(playerName, score, new Timestamp(System.currentTimeMillis()));

            try 
            {
                databaseConnection.connect(); // Establish the database connection
                databaseConnection.saveScore(scoreEntry);
                databaseConnection.disconnect(); // Close the database connection
            } 
            catch (SQLException e) 
            {
                System.err.println("Failed to save score: " + e.getMessage());
            }
            
            System.exit(0);
        }
        else
        {
            int currentLevel = model.getLevel();
            currentLevel++;

            //update level label
            view.updateLevelLabel(currentLevel);
            // Update the level in the model
            model.setLevel(currentLevel);
            // Reset necessary variables for the new level
            model.calculateRows(currentLevel);
            model.calculateColumns(currentLevel);
            model.setMaxColors(currentLevel);
            model.setTargetScore(currentLevel);
            model.setTotalSpecialBricks(model.getRows(), model.getColumns());

            // Initialize the new grid
            model.newLevelGrid();

            // Update the grid and points label in the view
            view.updateGrid(model.getGrid(), model.getRows(), model.getColumns());
            view.updatePointsLabel(model.getTotalScore());
        }
    }
}
    
