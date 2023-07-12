import javafx.scene.paint.Color;

public class Brick 
{
    private Color color;
    private int row;
    private int column;

    //Brick constructor
    public Brick(Color color, int row, int column)
    {
        this.color = color;
        this.row = row;
        this.column = column;
    }

    //Setters getters
    public Color getColor()
    {
        return color;
    }

    public void setColor(Color color)
    {
        this.color = color;
    }

    public int getRow()
    {
        return row;
    }

    public void setRow(int row)
    {
        this.row = row;
    }

    public int getColumn()
    {
        return column;
    }

    public void setColumn(int column)
    {
        this.column = column;
    }
}
