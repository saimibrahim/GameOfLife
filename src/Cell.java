/**
 * The smallest form of life.
 *
 * @author Saim Ibrahim
 */
public class Cell {
    //instance fields 
    private boolean isAlive;
    private int xCoordinate;
    private int yCoordinate;

    /*
     * Constructors
     */

    /**
     * Constructs this cell with the default value of alive being false.
     */
    public Cell() {
        isAlive = false;
        xCoordinate = 0;
        yCoordinate = 0;
    }

    /**
     * Constructs this cell with a specified location.
     *
     * @param xValue x coordinate of this cell
     * @param yValue y coordinate of this cell
     */
    public Cell(int xValue, int yValue) {
        isAlive = false;
        xCoordinate = xValue;
        yCoordinate = yValue;
    }

    /*
     * Accessors
     */

    /**
     * Returns the state of this cell.
     *
     * @return the state of this cell, false for dead and true for alive
     */
    public boolean getState() {
        return isAlive;
    }

    /**
     * Returns a string representation of this cell.
     *
     * @return a string representing this cell
     */
    public String toString() {
        return
                getClass().getName()
                        + "["
                        + "Alive: " + isAlive
                        + ", x Coordinate: " + xCoordinate
                        + ", y Coordinate: " + yCoordinate
                        + "]";
    }

    /*
     * Mutators
     */

    /**
     * Sets the state of this cell as <code>true</code>, also referred to as "alive."
     */
    public void setAlive() {
        isAlive = true;
    }

    /**
     * Sets the state of this cell as <code>false</code>, also referred to as "dead."
     */
    public void setDead() {
        isAlive = false;
    }

    /**
     * Sets the x coordinate of this cell.
     *
     * @param xValue x coordinate of this cell; cannot be negative
     */
    public void setX(int xValue) {
        xCoordinate = xValue;
    }

    /**
     * Sets the y coordinate of this cell.
     *
     * @param yValue y coordinate of this cell; cannot be negative
     */
    public void setY(int yValue) {
        yCoordinate = yValue;
    }

}