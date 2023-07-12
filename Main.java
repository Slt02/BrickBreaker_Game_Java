import java.sql.SQLException;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application
{
    public static void main(String[] args)
    {    
        String url = "jdbc:mysql://localhost:3306/scoredb?user=root";
        String username = "root";
        String password = "milko.1-2002";

        DatabaseConnection dbConnection = new DatabaseConnection(url, username, password);
        
        try 
        {
            dbConnection.connect();
        } 
        catch (SQLException e) 
        {
            System.err.println("Failed to connect to the database: " + e.getMessage());
        }

        launch(args);
    }

    @Override
    public void start(Stage primaryStage) 
    {   
        BrickBreakerModel model = new BrickBreakerModel(5, 5, 1, 4, 10, 2);
        BrickBreakerView view = new BrickBreakerView();
        BrickBreakerController controller = new BrickBreakerController(model, view, primaryStage);
    }
}