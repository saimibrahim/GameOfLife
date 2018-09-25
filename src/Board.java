import java.awt.*;
import java.util.Random;

/**
 * A board which encompasses and applies the rules of Conway's Game of Life.
 *
 * @author Saim Ibrahim
 */
public class Board {
    private static final char ALIVE = '\u002A';
    private static final char DEAD = '\u002D';
    private static final int MAXIMUM_X = 10;
    private static final int MAXIMUM_Y = 10;
    private static final Random generator = new Random();
    // class fields
    private static char[][] grid;
    //instance fields
    private Cell[][] cells;
    private boolean isStable;
    private int maximumX;
    private int maximumY;

    /**
     * Constructs a board with default values.
     */
    public Board() {
        maximumX = MAXIMUM_X;
        maximumY = MAXIMUM_Y;
        isStable = false;
        cells = new Cell[maximumX][maximumY];
        grid = new char[maximumX][maximumY];
        for (int xValue = 0; xValue < maximumX; xValue++) {
            for (int yValue = 0; yValue < maximumY; yValue++) {
                cells[xValue][yValue] = new Cell(xValue, yValue);
                grid[xValue][yValue] = DEAD;
            }
        }
    }

    /*
     * Constructors
     */

    /**
     * Constructs a board with specified size.
     *
     * @param length maximum length of board; cannot be negative
     * @param width  maximum width of board; cannot be negative
     */
    public Board(int length, int width) {
        maximumX = length;
        maximumY = width;
        cells = new Cell[maximumX][maximumY];
        grid = new char[maximumX][maximumY];
        for (int xValue = 0; xValue < maximumX; xValue++) {
            for (int yValue = 0; yValue < maximumY; yValue++) {
                cells[xValue][yValue] = new Cell(xValue, yValue);
                grid[xValue][yValue] = DEAD;
            }
        }
    }

    /**
     * Creates an instance of the Board class and starts a Game of Life simulation on a 10 by 10 grid   .
     *
     * @param argument not used
     */
    public static void main(String[] argument) throws InterruptedException {
        Board gameOfLife = new Board();
        gameOfLife.randomAlive();
        gameOfLife.randomAlive();
        final int DELAY = 1000;
        do {
            gameOfLife.makeGrid();
            gameOfLife.checker();
            Thread.sleep(DELAY);
            System.out.print("\f");
            Toolkit.getDefaultToolkit().beep();
        }
        while (true);
    }
    /*
     * Accessors
     */

    /**
     * Finds the amount of neighbours that the specified cell has.
     *
     * @param xValue x coordinate of the cell
     * @param yValue y coordinate of the cell
     * @return the amount of neighbours that the specified cell has
     */
    public int findNeighbours(int xValue, int yValue) {
        int numberOfNeighbours = 0;
        try {
            if (cells[xValue + 1][yValue - 1].getState()) {
                numberOfNeighbours++;
            }
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }

        try {
            if (cells[xValue + 1][yValue + 1].getState()) {
                numberOfNeighbours++;
            }
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }

        try {
            if (cells[xValue - 1][yValue - 1].getState()) {
                numberOfNeighbours++;
            }
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }

        try {
            if (cells[xValue - 1][yValue + 1].getState()) {
                numberOfNeighbours++;
            }
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }

        try {
            if (cells[xValue + 1][yValue].getState()) {
                numberOfNeighbours++;
            }
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }

        try {
            if (cells[xValue - 1][yValue].getState()) {
                numberOfNeighbours++;
            }
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }

        try {
            if (cells[xValue][yValue - 1].getState()) {
                numberOfNeighbours++;
            }
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }

        try {
            if (cells[xValue][yValue + 1].getState()) {
                numberOfNeighbours++;
            }
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }
        return numberOfNeighbours;
    }

    /**
     * Returns whether this board has a stable ecosystem of cells.
     */
    public boolean getStability() {
        return isStable;
    }

    /**
     * Returns the maximum x value of this Board.
     *
     * @return the maximum x value of this Board
     */
    public int getMaxX() {
        return maximumX;
    }

    /**
     * Returns the maximum y value of this Board.
     *
     * @return the maximum y value of this Board
     */
    public int getMaxY() {
        return maximumY;
    }

    /**
     * Returns state of specified cell with the cells array.
     *
     * @param xValue x coordinate of the cell
     * @param yValue y coordinate of the cell
     * @return state of specified cell with the cells array
     */
    public boolean getStateOfCell(int xValue, int yValue) {
        return cells[xValue][yValue].getState();
    }

    /**
     * Returns a string representation of this board.
     *
     * @return a string representing this board
     */
    public String toString() {
        return
                getClass().getName()
                        + "["
                        + "Cells: " + cells
                        + ", Maximum X: " + maximumX
                        + ", Maximum Y: " + maximumY
                        + "]";
    }
    /*
     * Mutators
     */

    /**
     * Sets the state of the cell objects based on the number of neighbours each cell object has.
     */
    public void checker() {
        Cell[][] temporaryCells = new Cell[maximumX][maximumY];
        Cell[][] currentCellCopy = cells;
        for (int xValue = 0; xValue < maximumX; xValue++) {
            for (int yValue = 0; yValue < maximumY; yValue++) {
                temporaryCells[xValue][yValue] = new Cell(xValue, yValue);
            }
        }

        for (int xValue = 0; xValue < maximumX; xValue++) {
            for (int yValue = 0; yValue < maximumY; yValue++) {
                if (cells[xValue][yValue].getState()) {
                    int numberOfNeighbours = findNeighbours(xValue, yValue);
                    if (numberOfNeighbours < 2 || numberOfNeighbours > 3) {
                        temporaryCells[xValue][yValue].setDead();
                    } else {
                        temporaryCells[xValue][yValue] = cells[xValue][yValue];
                    }
                } else if (!cells[xValue][yValue].getState()) {
                    int numberOfNeighbours = findNeighbours(xValue, yValue);
                    if (numberOfNeighbours == 3) {
                        temporaryCells[xValue][yValue].setAlive();
                    }
                }
            }
        }
        cells = temporaryCells;

        if (currentCellCopy.equals(cells)) {
            isStable = true;
        }
    }

    /**
     * Creates a grid of asterisks to represent cell objects.
     */
    public void makeGrid() {
        for (int xValue = 0; xValue < maximumX; xValue++) {
            for (int yValue = 0; yValue < maximumY; yValue++) {
                if (cells[xValue][yValue].getState()) {
                    grid[xValue][yValue] = ALIVE;
                } else if (!cells[xValue][yValue].getState()) {
                    grid[xValue][yValue] = DEAD;
                }
                System.out.print(grid[xValue][yValue]);
            }
            System.out.println();
        }
    }

    /**
     * Sets twenty random cells to be "alive."
     */
    public void randomAlive() {
        for (int counter = 0; counter < 50; counter++) {
            int randomX = generator.nextInt(maximumX);
            int randomY = generator.nextInt(maximumY);
            cells[randomX][randomY].setAlive();
        }
    }

    /**
     * Sets a specific cell to be "alive" inside the cells array.
     *
     * @param xValue x coordinate of the cell
     * @param yValue y coordinate of the cell
     */
    public void setCellAlive(int xValue, int yValue) {
        cells[xValue][yValue].setAlive();
    }

    /**
     * Sets a specific cell to be "dead" inside the cells array.
     *
     * @param xValue x coordinate of the cell
     * @param yValue y coordinate of the cell
     */
    public void setCellDead(int xValue, int yValue) {
        cells[xValue][yValue].setDead();
    }

    /**
     * Sets the size of the grid.
     *
     * @param xValue maximum length of grid; cannot be negative
     * @param yValue maximum width of grid; cannot be negative
     */
    public void setGridSize(int xValue, int yValue) {
        maximumX = xValue;
        maximumY = yValue;
    }
}