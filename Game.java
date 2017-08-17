/**
 * Models the game and handles all logic.
 *
 * @author Jake Koontz
 * Last update: 7/17/17
 */

public class Game {
    // constants used to represent values of space
    public static final int VOID = -2;  // space with no assigned value
    public static final int MINE = -1;

    // game properties
    private int height;
    private int width;
    private int numMines;

    private int[][] grid;   // array to represent values of spaces

    /**
     * Set properties of the game and initialize model of the game.
     *
     * @param height number of rows on grid
     * @param width number of columns on grid
     * @param numMines number of mines on grid
     */
    public Game(int height, int width, int numMines) {
        // set properties of game
        this.height = height;
        this.width = width;
        this.numMines = numMines;

        // initialize grid
        grid = new int[height][width];

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                grid[i][j] = VOID;
            }
        }
    }

    /**
     * Randomly place mines on the grid based on the first cell clicked.
     *
     * @param row vertical position of the first cell clicked
     * @param col horizontal position of the first cell clicked
     */
    public void placeMines(int row, int col) {
        int mineCount = 0;  // number of mines currently placed
        int randRow;
        int randCol;

        while (mineCount < numMines) {
            // randomly select spot on grid
            randRow = (int)(Math.random() * height);
            randCol = (int)(Math.random() * width);

            // verify that space does not already have a mine and is farther than one space away from first cell clicked
            if (grid[randRow][randCol] == VOID && (Math.abs(randRow - row) > 1 || Math.abs(randCol - col) > 1)) {
                grid[randRow][randCol] = MINE;  // set value of space
                mineCount++;
            }
        }
    }

    /**
     * Calculate and set number of mines adjacent to each space.
     */
    public void generateNumbers() {
        int adjMineCount = 0;   // number of mines adjacent to space

        // calculate number of adjacent mines
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                // check each adjacent space
                if (grid[i][j] != MINE) {
                    if (i > 0 && j > 0 && grid[i - 1][j - 1] == MINE)
                        adjMineCount++;
                    if (i > 0 && grid[i - 1][j] == MINE)
                        adjMineCount++;
                    if (i > 0 && j < width - 1 && grid[i - 1][j + 1] == MINE)
                        adjMineCount++;
                    if (j > 0 && grid[i][j - 1] == MINE)
                        adjMineCount++;
                    if (j < width - 1 && grid[i][j + 1] == MINE)
                        adjMineCount++;
                    if (i < height - 1 && j > 0 && grid[i + 1][j - 1] == MINE)
                        adjMineCount++;
                    if (i < height - 1 && grid[i + 1][j] == MINE)
                        adjMineCount++;
                    if (i < height - 1 && j < width - 1 && grid[i + 1][j + 1] == MINE)
                        adjMineCount++;

                    grid[i][j] = adjMineCount;  // set value of space
                    adjMineCount = 0;   // reset for next space
                }
            }
        }
    }

    /**
     * Get value of a specified space on the grid.
     *
     * @param row vertical location of space
     * @param col horizontal location of space
     * @return value of space
     */
    public int getValue(int row, int col) {
        // ensure valid location
        if (row < 0 || row >= height || col <  0 || col >= width)
            return VOID;

        return grid[row][col];
    }

    /**
     * Set value of a specified space on the grid.
     *
     * @param row vertical location of space
     * @param col horizontal location of space
     * @param value value of space
     */
    public void setValue(int row, int col, int value) {
        if (value >= VOID && value <= 8)    // check for valid value
            grid[row][col] = value;     // set value of space
    }

    // accessor methods

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public int getNumMines() {
        return numMines;
    }
}