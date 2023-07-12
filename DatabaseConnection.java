import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseConnection 
{
    private Connection connection;
    private String url;
    private String username;
    private String password;

    //Database Constructor
    public DatabaseConnection(String url, String username, String password) 
    {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    //Connecting to the database
    public void connect() throws SQLException 
    {
        connection = DriverManager.getConnection(url, username, password);
        System.out.println("Connected to the database.");
    }

    //Disconnecting from the database
    public void disconnect() throws SQLException 
    {
        if (connection != null && !connection.isClosed()) 
        {
            connection.close();
            System.out.println("Disconnected from the database.");
        }
    }

    //getter method
    public Connection getConnection() 
    {
        return connection;
    }
    
    //saving the score in the database
    public void saveScore(ScoreEntry scoreEntry) throws SQLException 
    {
        String sql = "INSERT INTO scores (player_name, score, timestamp) VALUES (?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) 
        {
            statement.setString(1, scoreEntry.getPlayerName());
            statement.setInt(2, scoreEntry.getScore());
            statement.setTimestamp(3, scoreEntry.getTimestamp());

            statement.executeUpdate();
        } 
        catch (SQLException e)
        {
            System.err.println("Failed to save score: " + e.getMessage());
            throw e;
        }
    }

}
