import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class SpecialBricks extends Brick
{
    private Image image;
    
    public SpecialBricks(Color color, int row, int col, Image image)
    {
        super(color,row,col);
        this.image = image;
    }
    //Image setter getter
    public Image getImage()
    {
        return image;
    }

    public void setImage(Image image) 
    {
        this.image = image;
    }
}
