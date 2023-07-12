import java.sql.Timestamp;

public class ScoreEntry 
{
    private String playerName;
    private int score;
    private Timestamp timestamp;

    public ScoreEntry(String playerName, int score, Timestamp timestamp) 
    {
        this.playerName = playerName;
        this.score = score;
        this.timestamp = timestamp;
    }

    //Setters and Getters
    public String getPlayerName()
    {
        return playerName;
    }

    public void setPlayerName(String playerName)
    {
        this.playerName = playerName;
    }

    public int getScore()
    {
        return score;
    }

    public void setScore(int score)
    {
        this.score = score;
    }

    public Timestamp getTimestamp() 
    {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) 
    {
        this.timestamp = timestamp;
    }
}
