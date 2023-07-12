import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class BrickBreakerModel 
{
    
    private int rows;
    private int columns;
    private int level;
    private int maxColors;
    private int targetScore;
    private int totalScore;
    private int totalSpecialBricks;
    private Brick[][] grid;

    public BrickBreakerModel(int rows, int columns, int level, int maxColors, int targetScore, int totalSpecialBricks)
    {
        this.rows = rows;
        this.columns = columns;
        this.level = level;
        this.maxColors = maxColors;
        this.targetScore = targetScore;
        this.totalScore = 0;
        this.totalSpecialBricks = totalSpecialBricks;
        this.grid = new Brick[rows][columns];
        initializeGrid(); //First initialization of the grid
    }

    //Grid initialization
    public void initializeGrid()
    {
        for (int row = 0; row < rows; row++)
        {
            for(int col = 0; col < columns; col++)
            {
                grid[row][col] = new Brick(getRandomColor(), row, col);
            }
        }

        addSpecialBricks();
    }

    //Add of special bricks in the grid
    private void addSpecialBricks()
    {
        Random random = new Random();
        for(int i = 0; i < totalSpecialBricks; i++)
        {
            int randomRow = random.nextInt(rows);
            int randomCol = random.nextInt(columns);
            Brick specialBrick = createRandomSpecialBrick(randomRow, randomCol);
            grid[randomRow][randomCol] = specialBrick;
        }
    }

    //Creation of a random special brick
    private SpecialBricks createRandomSpecialBrick(int row, int col)
    {
        Random random = new Random();
        int randomType = random.nextInt(5);

        // Load the image for the special brick
        Image image = loadImage(randomType);

        // Create and return an instance of the appropriate special brick subclass with the white color(all the same so they dont fit with regular ones) and image
        switch (randomType) 
        {
            case 0:
                return new BombBrick(Color.WHITE, row, col, image);
            case 1:
                return new ColorBombBrick(Color.GREY, row, col, image);
            case 2:
                return new JokerBrick(Color.ANTIQUEWHITE, row, col, image);
            case 3:
                return new NewLineBrick(Color.BISQUE, row, col, image);
            case 4:
                return new SuffleBrick(Color.BEIGE, row, col, image);
            default:
                return null;
        }
    }

    //loading the imaghes of the special bricks
    private Image loadImage(int type)
    {
        // Load the image for the special brick based on its type
        // You can use JavaFX's Image class to load the image from a file or resource
        // Example:
        String imagePath = "";
        switch (type) 
        {
            case 0:
                imagePath = "resources/BombBrick.png";
                break;
            case 1:
                imagePath = "resources/ColorBombBrick.png";
                break;
            case 2:
                imagePath = "resources/JokerBrick.png";
                break;
            case 3:
                imagePath = "resources/NewLineBrick.png";
                break;
            case 4:
                imagePath = "resources/SuffleBrick.png";
                break;
        }

        // Load the image using the image path
        Image image = new Image(imagePath);

        return image;
    }

    //Creates the new level's grid
    public void newLevelGrid()
    {
        grid = new Brick[rows][columns];
        for (int row = 0; row < rows; row++)
        {
            for(int col = 0; col < columns; col++)
            {
                grid[row][col] = new Brick(getRandomColor(), row, col);
            }
        }

        addSpecialBricks();
    }

    //Setters and getters
    public Brick[][] getGrid()
    {
        return grid;
    }

    public Brick getBrick(int row, int col) 
    {
        return grid[row][col];
    }

    public int getRows()
    {
        return rows;
    }

    public void setRows(int rows)
    {
        this.rows = rows;
    }

    public int getColumns()
    {
        return columns;
    }

    public void setColumns(int columns)
    {
        this.columns = columns;
    }

    public int getLevel()
    {
        return level;
    }

    public void setLevel(int level)
    {
       this.level = level;
    }
    
    public Color getRandomColor()
    {
        Color[] colors = {Color.BLUE, Color.PINK, Color.CYAN, Color.YELLOW, Color.GREEN,
            Color.MAGENTA, Color.ORANGE, Color.BLACK, Color.RED};

        Random random = new Random();
        int randomIndex = random.nextInt(maxColors);

        return colors[randomIndex];
    }

    public int getMaxColors()
    {
        return maxColors;
    }

    public void setMaxColors(int level)
    {
        this.maxColors = 4 + ((level - 1) / 2);
    }

   
    public int getTotalSpecialBricks() 
    {
       return totalSpecialBricks;
    }


    public void setTotalSpecialBricks(int rows, int cols)
    {
        this.totalSpecialBricks = ((rows * cols) * 5) / 100;
    }

    public int getTargetScore()
    {
        return targetScore;
    }

    public void setTargetScore(int level)
    {
        this.targetScore =  80 + (level * 20);
    }

    public int getTotalScore()
    {
        return totalScore;
    }

    public void setTotalScore(int totalScore)
    {
        this.totalScore = totalScore;
    }

    public void calculateRows(int level)
    {
        rows = 12 + level / 2;

        setRows(rows);
    }

    //Calculating new level's columns
    public void calculateColumns(int level)
    {
        columns = 14 + ((level - 1) / 2);

        setColumns(columns);
    }
    
    //Checks if the cell that we clicked is in a valid position
    public boolean isValidPosition(int row, int col) 
    {
        return row >= 0 && row < rows && col >= 0 && col < columns;
    }

    //Handles the click on the regular bricks
    public void handleBricks(int row, int col)
    {
        if(isValidPosition(row, col) && !getBrick(row, col).getColor().equals(Color.TRANSPARENT))
        {
            Color targetColor = getBrick(row, col).getColor();
            
            if(hasSameColorNeighbors(row, col, targetColor))
            {
                int removedBricks = removeBricks(row, col, targetColor);
                if(removedBricks > 0)
                {
                    shiftBricksDown();
                    shiftColumnsLeft();
                    calculatePoints(removedBricks);
                }
            }
        }
    }

    //Checks if the cell has Same color neighbors
    private boolean hasSameColorNeighbors(int row, int col, Color targetColor)
    {
        return isValidPosition(row - 1, col) && getBrick(row - 1, col).getColor().equals(targetColor) ||
        isValidPosition(row + 1, col) && getBrick(row + 1, col).getColor().equals(targetColor)  ||
        isValidPosition(row, col - 1) && getBrick(row, col - 1).getColor().equals(targetColor)  ||
        isValidPosition(row, col + 1) && getBrick(row, col + 1).getColor().equals(targetColor);
    }

    //Removes the neighboring bricks
    private int removeBricks(int row, int col, Color targetColor)
    {
        if (!isValidPosition(row, col) || !getBrick(row, col).getColor().equals(targetColor))
        {   
            return 0;
        }
        
        int removedBricks = 1;
        getBrick(row, col).setColor(Color.TRANSPARENT);

        removedBricks += removeBricks(row - 1, col, targetColor);
        removedBricks += removeBricks(row + 1, col, targetColor);
        removedBricks += removeBricks(row, col - 1, targetColor);
        removedBricks += removeBricks(row, col + 1, targetColor);
        
        return removedBricks;
    }

    //Checks if shift is needed and shifts the bricks down
    private void shiftBricksDown()
    {
        for(int col = 0; col < columns; col++ )
        {
            int emptyRow = -1;
            for(int row = rows - 1; row >= 0; row--)
            {
                Brick currBrick = getBrick(row, col);

                if(currBrick.getColor().equals(Color.TRANSPARENT)) // If the current brick is transparent, set emptyRow if not already set
                {
                    if(emptyRow == -1)
                    {
                        emptyRow = row;
                    }
                }
                else if(emptyRow != -1)
                {
                    if (currBrick instanceof SpecialBricks) //Checking for every type of Special Brick
                    {
                        if(currBrick instanceof BombBrick)
                        {
                            BombBrick currSpecialBrick = (BombBrick) currBrick;
                            BombBrick targetSpecialBrick = new BombBrick(Color.WHITE, emptyRow, col, null);

                            targetSpecialBrick.setImage(currSpecialBrick.getImage());
                            currSpecialBrick.setColor(Color.TRANSPARENT);
                            currSpecialBrick.setImage(null);

                            grid[emptyRow][col] = targetSpecialBrick;
                            grid[row][col] = new Brick(Color.TRANSPARENT, row, col);

                            emptyRow--;
                        }
                        else if(currBrick instanceof ColorBombBrick)
                        {
                            ColorBombBrick currSpecialBrick = (ColorBombBrick) currBrick;
                            ColorBombBrick targetSpecialBrick = new ColorBombBrick(Color.GREY, emptyRow, col, null);

                            targetSpecialBrick.setImage(currSpecialBrick.getImage());
                            currSpecialBrick.setColor(Color.TRANSPARENT);
                            currSpecialBrick.setImage(null);

                            grid[emptyRow][col] = targetSpecialBrick;
                            grid[row][col] = new Brick(Color.TRANSPARENT, row, col);

                            emptyRow--;
                        }
                        else if(currBrick instanceof JokerBrick)
                        {
                            JokerBrick currSpecialBrick = (JokerBrick) currBrick;
                            JokerBrick targetSpecialBrick = new JokerBrick(Color.ANTIQUEWHITE, emptyRow, col, null);

                            targetSpecialBrick.setImage(currSpecialBrick.getImage());
                            currSpecialBrick.setColor(Color.TRANSPARENT);
                            currSpecialBrick.setImage(null);

                            grid[emptyRow][col] = targetSpecialBrick;
                            grid[row][col] = new Brick(Color.TRANSPARENT, row, col);

                            emptyRow--;
                        }
                        else if(currBrick instanceof SuffleBrick)
                        {
                            SuffleBrick currSpecialBrick = (SuffleBrick) currBrick;
                            SuffleBrick targetSpecialBrick = new SuffleBrick(Color.BEIGE, emptyRow, col, null);
                            
                            targetSpecialBrick.setImage(currSpecialBrick.getImage());
                            currSpecialBrick.setColor(Color.TRANSPARENT);
                            currSpecialBrick.setImage(null);
                            
                            grid[emptyRow][col] = targetSpecialBrick;
                            grid[row][col] = new Brick(Color.TRANSPARENT, row, col);
                            
                            emptyRow--;
                        }
                        else if(currBrick instanceof NewLineBrick)
                        {
                            NewLineBrick currSpecialBrick = (NewLineBrick) currBrick;
                            NewLineBrick targetSpecialBrick = new NewLineBrick(Color.BISQUE, emptyRow, col, null);
                            
                            targetSpecialBrick.setImage(currSpecialBrick.getImage());
                            currSpecialBrick.setColor(Color.TRANSPARENT);
                            currSpecialBrick.setImage(null);
                            
                            grid[emptyRow][col] = targetSpecialBrick;
                            grid[row][col] = new Brick(Color.TRANSPARENT, row, col);
                            
                            emptyRow--;
                        }
                    } 
                    else
                    { 
                        Brick targetBrick = getBrick(emptyRow, col);
                        targetBrick.setColor(currBrick.getColor());
                        currBrick.setColor(Color.TRANSPARENT);

                        grid[emptyRow][col] = targetBrick;
                        targetBrick.setRow(emptyRow);
                        emptyRow--;
                    }
                }
            }
        }
    }
    
    //Chekc for empty columns and shift them to the left
    private void shiftColumnsLeft()
    {
        for (int col = 0; col < columns; col++)
        {
            boolean isEmpty = true;
    
            // Check if the column is filled with transparent bricks
            for (int row = 0; row < rows; row++)
            {
                if (!getBrick(row, col).getColor().equals(Color.TRANSPARENT))
                {
                    isEmpty = false;
                    break;
                }
            }
    
            // If the column is empty, shift the non-transparent columns to the left
            if (isEmpty)
            {
                // Shift all subsequent non-empty columns to the left
                int shiftCol = col + 1;
                while (shiftCol < columns && checkEmptyColumn(shiftCol))
                {
                    shiftCol++;
                }
    
                if (shiftCol < columns)
                {
                    for (int row = 0; row < rows; row++)
                    {
                        Brick currBrick = getBrick(row, shiftCol);
                        Brick targetBrick = getBrick(row, col);

                        if(currBrick instanceof SpecialBricks) //Checking for every type of Special Brick for shifting to the left
                        {
                            if(currBrick instanceof BombBrick)
                            {
                                BombBrick currSpecialBrick = (BombBrick) currBrick;
                                BombBrick targetSpecialBrick = new BombBrick(Color.WHITE, row, shiftCol, null);

                                targetSpecialBrick.setImage(currSpecialBrick.getImage());
                                currSpecialBrick.setColor(Color.TRANSPARENT);
                                currSpecialBrick.setImage(null);

                                grid[row][col] = targetSpecialBrick;
                                grid[row][col + 1] = new Brick(Color.TRANSPARENT, row, col + 1); 
                            }
                            else if(currBrick instanceof ColorBombBrick)
                            {
                                ColorBombBrick currSpecialBrick = (ColorBombBrick) currBrick;
                                ColorBombBrick targetSpecialBrick = new ColorBombBrick(Color.AQUAMARINE, row, shiftCol, null);

                                targetSpecialBrick.setImage(currSpecialBrick.getImage());
                                currSpecialBrick.setColor(Color.TRANSPARENT);
                                currSpecialBrick.setImage(null);

                                grid[row][col] = targetSpecialBrick;
                                grid[row][col + 1] = new Brick(Color.TRANSPARENT, row, col + 1);
                            }
                            else if(currBrick instanceof JokerBrick)
                            {
                                JokerBrick currSpecialBrick = (JokerBrick) currBrick;
                                JokerBrick targetSpecialBrick = new JokerBrick(Color.ANTIQUEWHITE, row, shiftCol, null);

                                targetSpecialBrick.setImage(currSpecialBrick.getImage());
                                currSpecialBrick.setColor(Color.TRANSPARENT);
                                currSpecialBrick.setImage(null);

                                grid[row][col] = targetSpecialBrick;
                                grid[row][col + 1] = new Brick(Color.TRANSPARENT, row, col + 1);
                            }
                            else if(currBrick instanceof SuffleBrick)
                            {
                                SuffleBrick currSpecialBrick = (SuffleBrick) currBrick;
                                SuffleBrick targetSpecialBrick = new SuffleBrick(Color.BEIGE, row, shiftCol, null);

                                targetSpecialBrick.setImage(currSpecialBrick.getImage());
                                currSpecialBrick.setColor(Color.TRANSPARENT);
                                currSpecialBrick.setImage(null);

                                grid[row][col] = targetSpecialBrick;
                                grid[row][col + 1] = new Brick(Color.TRANSPARENT, row, col + 1);
                            }
                            else if(currBrick instanceof NewLineBrick)
                            {
                                NewLineBrick currSpecialBrick = (NewLineBrick) currBrick;
                                NewLineBrick targetSpecialBrick = new NewLineBrick(Color.BISQUE, row, shiftCol, null);

                                targetSpecialBrick.setImage(currSpecialBrick.getImage());
                                currSpecialBrick.setColor(Color.TRANSPARENT);
                                currSpecialBrick.setImage(null);

                                grid[row][col] = targetSpecialBrick;
                                grid[row][col + 1] = new Brick(Color.TRANSPARENT, row, col + 1);
                            }
                        }
                        else
                        {
                            targetBrick.setColor(currBrick.getColor());
                            currBrick.setColor(Color.TRANSPARENT);
                            targetBrick.setColumn(col);
                        }
                    }
                }
            }
        }
    }
    
    //Checks if the column is empty
    private boolean checkEmptyColumn(int col)
    {
        for (int row = 0; row < rows; row++)
        {
            if (!getBrick(row, col).getColor().equals(Color.TRANSPARENT))
            {
                return false;
            }
        }
        return true;
    }

    //Checks if all bricks possible moves are exhausted
    public boolean movesExhausted()
    {
        for(int row = 0; row < rows; row++)
        {
            for(int col = 0; col < columns; col++)
            {
                if(canRemoveBricks(row, col) || (getBrick(row, col) instanceof BombBrick || getBrick(row, col)
                instanceof ColorBombBrick || getBrick(row, col) instanceof JokerBrick || getBrick(row, col) instanceof SuffleBrick || getBrick(row, col) instanceof NewLineBrick))
                {
                    return false;
                }
            }
        }

        return true;
    }

    //Check is we can remove the brick at the given position
    private boolean canRemoveBricks(int row, int col)
    {
        Color targetColor = getBrick(row, col).getColor();
        return isValidPosition(row, col) && !targetColor.equals(Color.TRANSPARENT) && hasSameColorNeighbors(row, col, targetColor);
    }
    
    //Calculates the points of the game per level
    public void calculatePoints(int removedBricks)
    {
        int currentScore = 0;
        if(removedBricks <= 4)
        {
            currentScore += removedBricks;
        }
        else if(removedBricks >= 5 && removedBricks <= 12)
        {
            currentScore += (int) (1.5 * removedBricks);
        }
        else if(removedBricks > 12)
        {
            currentScore += 2 * removedBricks;
        }

        totalScore += currentScore;
        setTotalScore(totalScore);
    }

    //Checks if the game is over or not 
    public boolean GameOver()
    {
        return totalScore < targetScore;
    }

    //handle the actions of the special bricks
    public void handleSpecialBrick(int row, int col, SpecialBricks specialBrick) 
    {
        if(specialBrick instanceof BombBrick)
        {
            specialBrick.setImage(null);
            specialBrick.setColor(Color.TRANSPARENT);
            grid[row][col] = new Brick(Color.TRANSPARENT, row, col);
            
            bombBrickAction(row, col);
            shiftBricksDown();
            shiftColumnsLeft();
        }
        else if(specialBrick instanceof ColorBombBrick)
        {
            specialBrick.setImage(null);
            specialBrick.setColor(Color.TRANSPARENT);
            grid[row][col] = new Brick(Color.TRANSPARENT, row, col);
            
            ColorBombBrickAction(row, col);
            shiftBricksDown();
            shiftColumnsLeft();
        }
        else if(specialBrick instanceof JokerBrick) //When clicking on a joker brick the color of it changes as needed (so the most possible bricks break)
        {
            specialBrick.setImage(null);
            specialBrick.setColor(Color.TRANSPARENT);

            Color targetColor = findSuitableColor(row, col);
            grid[row][col] = new Brick(targetColor, row, col);

            shiftBricksDown();
            shiftColumnsLeft();
        }
        else if(specialBrick instanceof SuffleBrick)
        {
            specialBrick.setImage(null);
            specialBrick.setColor(Color.TRANSPARENT);

            suffleBrickAction(row, col);
        }
        else if(specialBrick instanceof NewLineBrick)
        {
            if(isTopRowClear(row, col))
            {
                specialBrick.setImage(null);
                specialBrick.setColor(Color.TRANSPARENT);
                grid[row][col] = new Brick(Color.TRANSPARENT, row, col);

                newLineBrickAction(row, col);
                shiftBricksDown();
                shiftColumnsLeft();
            }
        }
    }

    //The action when we click a bomb brick
    private void bombBrickAction(int row, int col)
    {
        int bricksRemoved = 1;
        
        bricksRemoved += bombBrickDelete(row - 1, col);
        bricksRemoved += bombBrickDelete(row + 1, col);
        bricksRemoved += bombBrickDelete(row, col - 1);
        bricksRemoved += bombBrickDelete(row, col + 1);

        calculatePoints(bricksRemoved);
    }

    //Deletes the bricks that have to get deleted from the bomb brick
    private int bombBrickDelete(int row, int col)
    {
        if(isValidPosition(row, col))
        {
            //case that a special brick is going to be deleted
            if(getBrick(row, col) instanceof SpecialBricks)
            {
                SpecialBricks specialBrick = (SpecialBricks) getBrick(row, col);
                specialBrick.setImage(null);
                specialBrick.setColor(Color.TRANSPARENT);
                grid[row][col] = new Brick(Color.TRANSPARENT, row, col);

                return 1;
            }
            else
            {
                Brick brick = getBrick(row, col);
                brick.setColor(Color.TRANSPARENT);

                return 1;
            }
        }
        return 0;
    }
    
    private void ColorBombBrickAction(int row, int col)
    {
        int removedBricks = 1;

        Color targetColor = compareAdjColors(row, col);
        removedBricks += colorBombDeletions(row, col, targetColor);

        calculatePoints(removedBricks);
    }

    //Compares the colors of the adjacent bricks so that we can find the one that is present the most times (targetColor)
    private Color compareAdjColors(int row, int col)
    {
        Set<Color> colors = new HashSet<Color>();
        Color targetColor = null;
        int maxDeletions = 1;

        for(int r = 0; r < rows; r++)
        {
            for(int column = 0; column < columns; column++)
            {
                colors.add(getBrick(r, column).getColor());
            }
        }

        for(Color color : colors)
        {
            if(color != Color.TRANSPARENT)
            {
                int deletions = countColorDeletions(row, col, color);
                if(deletions > maxDeletions)
                {
                    maxDeletions = deletions;
                    targetColor = color;
                }
            }
        }

        return targetColor;
    }

    //Counts the number of bricks that might get deleted, depending on their color
    private int countColorDeletions(int row, int col, Color targetColor)
    {
        int deletions = 0;   
        // Check the adjacent bricks for potential deletions
        if (isValidPosition(row - 1, col) && getBrick(row - 1, col).getColor() == targetColor) 
        {
            deletions++;
        }

        if (isValidPosition(row + 1, col) && getBrick(row + 1, col).getColor() == targetColor) 
        {
            deletions++;
        }

        if (isValidPosition(row, col - 1) && getBrick(row, col - 1).getColor() == targetColor) 
        {
            deletions++;
        }

        if (isValidPosition(row, col + 1) && getBrick(row, col + 1).getColor() == targetColor) 
        {
            deletions++;
        }

        return deletions;
    }

    //Deletes the bricks that have to get deleted from the color bomb brick and counts the number or bricks that got deleted so we can count the score 
    private int colorBombDeletions(int row, int col, Color targetColor)
    {
        int deletions = 0;

        // Check the adjacent bricks if they have the same color as the target color and if yes, they get deleted
        if (isValidPosition(row - 1, col) && getBrick(row - 1, col).getColor() == targetColor) 
        {
            getBrick(row - 1, col).setColor(Color.TRANSPARENT);
            deletions++;
        }

        if (isValidPosition(row + 1, col) && getBrick(row + 1, col).getColor() == targetColor) 
        {
            getBrick(row + 1, col).setColor(Color.TRANSPARENT);
            deletions++;
        }

        if (isValidPosition(row, col - 1) && getBrick(row, col - 1).getColor() == targetColor) 
        {
            getBrick(row, col - 1).setColor(Color.TRANSPARENT);
            deletions++;
        }

        if (isValidPosition(row, col + 1) && getBrick(row, col + 1).getColor() == targetColor) 
        {
            getBrick(row, col + 1).setColor(Color.TRANSPARENT);
            deletions++;
        }

        return deletions;
    }

    //Checks if the top row of the grid is clear
    private boolean isTopRowClear(int row, int col)
    {
        for (int column = 0; column < columns; column++) 
        {
            if (!getBrick(0, column).getColor().equals(Color.TRANSPARENT)) 
            {
                return false;
            }
        }
        return true;
    }
    
    //New line brick's action
    private void newLineBrickAction(int row, int col) 
    {
        for(int column = 0; column < columns; column++)
        {
            grid[0][column] = new Brick(getRandomColor(), 0, column);
        }

        calculatePoints(1);
    }

    //Suffle brick's action
    private void suffleBrickAction(int row, int col)
    {
        grid[row][col] = new Brick(getRandomColor(), row, col);
    }

    //Finding the the most suitable color for the joker brick
    private Color findSuitableColor(int row, int col)
    {
        Set<Color> colors = new HashSet<Color>();
        Color targetColor = null;
        int maxDeletions = 0;

        for(int r = 0; r < rows; r++)
        {
            for(int column = 0; column < columns; column++)
            {
                colors.add(getBrick(r, column).getColor());
            }
        }

        for(Color color : colors)
        {
            if(color != Color.TRANSPARENT)
            {
                int deletions = getPotentialDeletions(row, col, color);
                if(deletions > maxDeletions)
                {
                    maxDeletions = deletions;
                    targetColor = color;
                }
            }
        }

        return targetColor;
    }

    // Check the adjacent bricks for potential deletions
    private int getPotentialDeletions(int row, int col, Color targetColor) 
    {
        int deletions = 0;
    
        deletions += countDeletions(row - 1, col, targetColor, new HashSet<>());
        deletions += countDeletions(row + 1, col, targetColor, new HashSet<>());
        deletions += countDeletions(row, col - 1, targetColor, new HashSet<>());
        deletions += countDeletions(row, col + 1, targetColor, new HashSet<>());
    
        return deletions;
    }

    //Method to traverse the neighboring bricks and keep track of the visited bricks using a a Set (visited) 
    private int countDeletions(int row, int col, Color targetColor, Set<Brick> visited) 
    {
        if (isValidPosition(row, col) && !visited.contains(getBrick(row, col))) 
        {
            Brick brick = getBrick(row, col);
            visited.add(brick);
    
            if (brick.getColor() == targetColor) 
            {
                int deletions = 1;
    
                deletions += countDeletions(row - 1, col, targetColor, visited);
                deletions += countDeletions(row + 1, col, targetColor, visited);
                deletions += countDeletions(row, col - 1, targetColor, visited);
                deletions += countDeletions(row, col + 1, targetColor, visited);
    
                return deletions;
            }
        }
    
        return 0;
    }
}